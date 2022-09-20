package com.rviewer.skeletons.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rviewer.skeletons.model.Image;
import com.rviewer.skeletons.service.ImageService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ImageController {
	private ImageService imageService;
	
	@PostMapping("/images")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void createImage(@RequestBody List<Image> images){
		imageService.saveAll(images);
	}
}
