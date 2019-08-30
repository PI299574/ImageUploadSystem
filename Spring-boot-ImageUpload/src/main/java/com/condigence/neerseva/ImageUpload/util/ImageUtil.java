package com.condigence.neerseva.ImageUpload.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {

	// private final Path rootLocation = Paths.get("filestorage"); //in context with
	// Project Path
	String directory = "F:\\Directory1";
	private final Path rootLocation = Paths.get(directory); // External Path of System
	public static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

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

	public void storeImageinDirectory(MultipartFile file) throws IOException {
		logger.info("*************file name******************* " + file.getInputStream());
		logger.info("*******original file path *****" + this.rootLocation.resolve(file.getOriginalFilename()));
		try {
			Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] getImageWithFileName(String fileName) {
		logger.info("In ImageService:::::getImageWithFileName");
		final ImageFilter imageFilter = new ImageFilter();
		byte[] imagePic = null;
		File file = null;
		final File dir = new File(directory);
		for (final File imgFile : dir.listFiles()) {
			if (imageFilter.accept(imgFile)) {
				if (fileName.equals(imgFile.getName())) {
					file = imgFile;
					try {
						imagePic = getImagePic(imgFile);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					logger.info("*******Image Not Found || upload image is not in correct format********");
				}
			}
		}
		return imagePic;
	}

	private byte[] getImagePic(File imgFile) throws FileNotFoundException {
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

	// Not in use
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	// Not In use
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
}
