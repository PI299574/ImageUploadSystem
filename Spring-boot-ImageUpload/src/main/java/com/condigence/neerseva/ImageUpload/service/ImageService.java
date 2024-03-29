package com.condigence.neerseva.ImageUpload.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.condigence.neerseva.ImageUpload.entity.ImageModel;
import com.condigence.neerseva.ImageUpload.repository.ImageRepository;
import com.condigence.neerseva.ImageUpload.util.ImageUtil;

@Service
public class ImageService {

	@Autowired
	ImageRepository imageRepository;
	public static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public ImageModel store(MultipartFile file) throws IOException {

		// To store at external/internal directory
		ImageUtil imgutil = new ImageUtil();
		imgutil.createDirectory();
		try {
			imgutil.storeImageinDirectory(file);
		} catch (Exception e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
		// To Store into DB
		ImageModel image = new ImageModel();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		logger.info("*************file name******************* " + fileName);
		image.setName(fileName);
		image.setType(file.getContentType());
		image.setImagePath(ImageUtil.rootLocation.resolve(file.getOriginalFilename()).toString());
		image.setImageSize(file.getSize());
		image.setImageName(fileName.substring(0, 3) + "_" + imgutil.getDateTimeFormatter());
		image.setPic(file.getBytes());// not storing the byte since byte length is too
		// long

		System.out.println("Image is " + image);
		final ImageModel savedImage = imageRepository.save(image);
		logger.info("***************Image saved*******************");
		return savedImage;
	}

	public ImageModel getImage(Long id) {
		logger.info("In ImageService:::::getImage");
		ImageModel image = null;
		ImageUtil imgutil = new ImageUtil();
		try {
			image = imageRepository.findById(id).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != image) {
			if (null == image.getPic()) {
				image.setPic(imgutil.getImageWithFileName(image.getName()));
			}
		}
		logger.info("Image is " + image);
		return image;

	}

	public ImageModel getImageId(String name) {
		// TODO Auto-generated method stub
		logger.info("In ImageService:::::getImageId");
		ImageUtil imgutil = new ImageUtil();
		ImageModel image = null;
		try {
			image = imageRepository.getimageId(name).get();

			logger.info("Image detail is " + image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != image) {
			if (null == image.getPic()) {
				image.setPic(imgutil.getImageWithFileName(image.getName()));
			}
		}
		return image;
	}

	public List<ImageModel> findAll() {
		// TODO Auto-generated method stub
		List<ImageModel> imageList = imageRepository.findAll();
		ImageUtil imgutil = new ImageUtil();
		for (ImageModel image : imageList) {
			if (null == image.getPic()) {
				image.setPic(imgutil.getImageWithFileName(image.getName()));
			}
		}
		return imageList;
	}
}
