package com.rviewer.skeletons.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rviewer.skeletons.dao.ImageRepository;
import com.rviewer.skeletons.model.Image;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ImageService {
	private ImageRepository imageRepo;
	
	public void saveAll(List<Image> images) {
		imageRepo.saveAll(images);
	}
}
