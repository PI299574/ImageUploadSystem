package com.condigence.neerseva.ImageUpload.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.condigence.neerseva.ImageUpload.entity.ImageModel;
import com.condigence.neerseva.ImageUpload.service.ImageService;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

	@Autowired
	ImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<?> uplaodImage(@RequestParam("myFile") MultipartFile file) throws Exception {

		String message = "";
		Resource resource = null;
		File file1 = null;
		ImageModel image = null;
		ImageModel image1 = new ImageModel();
		byte[] pic = null;
		try {
			image = imageService.store(file);
			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			System.out.println(message);
			// resource = imageService.getImageFile(file.getOriginalFilename());
			pic = imageService.getImageWithFileName(file.getOriginalFilename());

		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			e.printStackTrace(System.out);
			throw new Exception(e);
		}
		// store(file);
		image1.setPic(pic);
		image1.setId(image.getId());
		image1.setType(file.getContentType());
		image1.setName(file.getOriginalFilename());
		return ResponseEntity.status(HttpStatus.OK).body(image1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/{id}")
	public ResponseEntity<?> getImageWithId(@RequestParam("id") Long id) {
		byte[] image = imageService.getImage(id);
		if (null == image) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<byte[]>(image, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/{name}")
	public ResponseEntity<?> getImageWithName(@RequestParam("name") String name) {
		Resource resource = imageService.getImageFile(name);
		return new ResponseEntity<Resource>(resource, HttpStatus.OK);

	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// @GetMapping(value = "/all")
	// public ResponseEntity<List<ImageModel>> listAllImages() {
	// List<ImageModel> imageList = imageRepository.findAll();

//		if (imageList.isEmpty()) {
//			return new ResponseEntity(HttpStatus.NO_CONTENT);
//		}
	// return new ResponseEntity<List<ImageModel>>(imageList, HttpStatus.OK);
	// }

}
