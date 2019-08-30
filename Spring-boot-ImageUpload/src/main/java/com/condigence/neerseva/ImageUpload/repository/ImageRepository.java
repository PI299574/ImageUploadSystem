package com.condigence.neerseva.ImageUpload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.condigence.neerseva.ImageUpload.entity.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {

}
