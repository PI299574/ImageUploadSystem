package com.condigence.neerseva.ImageUpload.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.condigence.neerseva.ImageUpload.config.CustomErrorType;
import com.condigence.neerseva.ImageUpload.entity.ImageModel;
import com.condigence.neerseva.ImageUpload.service.ImageService;
import com.condigence.neerseva.ImageUpload.util.ImageUtil;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

	@Autowired
	ImageService imageService;

	public static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/upload")
	public ResponseEntity<?> uplaodImage(@RequestParam("myFile") MultipartFile file) throws Exception {
		logger.info("In ImageController:::::uplaodImage*******************");
		String message = "";
		ImageModel savedImageObj = null;
		ImageModel image = new ImageModel();
		ImageUtil imgutil = new ImageUtil();
		byte[] _pic = null;
		try {
			// save image into db and directory
			savedImageObj = imageService.store(file);
			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			logger.info(message);
			_pic = imgutil.getImageWithFileName(file.getOriginalFilename());
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			logger.warn(message);
			throw new Exception(e);
		}
		// Now return back the saved image from directory
		if (null != _pic) {
			image.setPic(_pic);
			image.setId(savedImageObj.getId());
			image.setType(file.getContentType());
			image.setName(file.getOriginalFilename());
			image.setImageName(savedImageObj.getImageName());
			image.setImageSize(savedImageObj.getImageSize());
			image.setImagePath(savedImageObj.getImagePath());

		} else {
			return new ResponseEntity(new CustomErrorType("Image Not Found || Uploaded image is not in correct format"),
					HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}

	@PostMapping("/db/upload")
	public ResponseEntity<?> uplaodImageInDB(@RequestParam("myFile") MultipartFile file) throws Exception {
		logger.info("In ImageController:::::uplaodImageInDB*******************");
		String message = "";
		ImageModel savedImageObj = null;
		try {
			// save image into db and directory
			savedImageObj = imageService.store(file);
			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			logger.info(message);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			logger.warn(message);
			throw new Exception(e);
		}
		return ResponseEntity.status(HttpStatus.OK).body(savedImageObj);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getImageWithId(@RequestParam("id") Long id) {
		logger.info("In ImageController:::::getImageWithId*******************");
		ImageModel image = null;
		try {
			image = imageService.getImage(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == image) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
		// return new ResponseEntity<ImageModel>(image, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}

	@GetMapping("/db/{id}")
	public ResponseEntity<?> getImageWithIdFromDb(@RequestParam("id") Long id) {
		logger.info("In ImageController:::::getImageWithIdFromDb*******************");
		ImageModel image = null;
		try {
			image = imageService.getImage(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == image) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
		// return new ResponseEntity<ImageModel>(image, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}

	@GetMapping("/{imageName}")
	public ResponseEntity<?> getImageWithName(@RequestParam("imageName") String imageName) {
		logger.info("In ImageController:::::getImageWithName******************");
		ImageModel image = null;
		try {
			image = imageService.getImageId(imageName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == image) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
		// return new ResponseEntity<ImageModel>(image, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping(value = "/all")
	public ResponseEntity<?> listAllImages() {
		List<ImageModel> imageList = imageService.findAll();

		if (imageList.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ImageModel>>(imageList, HttpStatus.OK);
	}

}
