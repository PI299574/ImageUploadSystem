package com.condigence.neerseva.ImageUpload.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.condigence.neerseva.ImageUpload.entity.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {

	@Query("SELECT img FROM ImageModel img where img.imageName = :imageName")
	Optional<ImageModel> getimageId(@Param("imageName") String imageName);

}
