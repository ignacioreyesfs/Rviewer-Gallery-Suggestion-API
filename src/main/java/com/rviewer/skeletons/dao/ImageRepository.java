package com.rviewer.skeletons.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rviewer.skeletons.model.image.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, String>{

}
