package com.condigence.neerseva.ImageUpload.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.condigence.neerseva.ImageUpload.entity.ImageModel;
import com.condigence.neerseva.ImageUpload.repository.ImageRepository;
import com.condigence.neerseva.ImageUpload.util.ImageFilter;

@Service
public class ImageService {

	@Autowired
	ImageRepository imageRepository;

	// private final Path rootLocation = Paths.get("filestorage"); //in context with
	// Project Path
	String directory = "F:\\Directory1";
	private final Path rootLocation = Paths.get(directory); // External Path of System

	public void createDirectory() {
		// To create single directory/folder
		File file = new File("F:\\Directory1");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	public ImageModel store(MultipartFile file) throws IOException {
		createDirectory();
		try {
			System.out.println("111111111111111" + file.getInputStream());
			System.out.println("222222222222222" + this.rootLocation.resolve(file.getOriginalFilename()));
			Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}

		ImageModel img = new ImageModel();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println("File name " + fileName);
		img.setName(fileName);
		img.setType(file.getContentType());
		// img.setPic(file.getBytes());
		final ImageModel savedImage = imageRepository.save(img);
		System.out.println("Image saved");
		return savedImage;
	}

	public Resource getImageFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	public byte[] getImage(Long id) {
		byte[] image = imageRepository.findById(id).get().getPic();
		return image;

	}

	public byte[] getImageWithFileName(String fileName) {
		final ImageFilter imageFilter = new ImageFilter();
		byte[] image = null;
		File file = null;

		final File dir = new File(directory);
		for (final File imgFile : dir.listFiles()) {
			if (imageFilter.accept(imgFile)) {
				System.out.println(imgFile.getName());
				if (fileName.equals(imgFile.getName())) {
					file = imgFile;
					try {
						image = doSomethingWithImgFile(imgFile);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("Image Not Found****");
				}

			}
		}
		return image;
	}

	private byte[] doSomethingWithImgFile(File imgFile) throws FileNotFoundException {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream(imgFile);
		// create FileInputStream which obtains input bytes from a file in a file system
		// FileInputStream is meant for reading streams of raw bytes such as image data.
		// For reading streams of characters, consider using FileReader.

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				// Writes to this byte array output stream
				bos.write(buf, 0, readNum);
				System.out.println("read " + readNum + " bytes,");
			}
		} catch (IOException ex) {
			// Logger.getLogger(ConvertImage.class.getName()).log(Level.SEVERE, null, ex);
		}

		byte[] bytes = bos.toByteArray();
		return bytes;
	}
}
