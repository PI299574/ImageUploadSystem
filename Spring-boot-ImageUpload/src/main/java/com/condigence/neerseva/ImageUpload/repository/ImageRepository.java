package com.condigence.neerseva.ImageUpload.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.condigence.neerseva.ImageUpload.entity.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {

	@Query("SELECT img.id FROM ImageModel img where img.name = :name")
	Optional<ImageModel> getimageId(@Param("name") String name);

}
