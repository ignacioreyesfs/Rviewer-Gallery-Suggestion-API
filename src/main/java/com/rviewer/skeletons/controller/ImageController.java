package com.rviewer.skeletons.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rviewer.skeletons.model.Image;
import com.rviewer.skeletons.service.EventDTO;
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
	
	@PostMapping("/images/{imageId}/events")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void addEvent(@PathVariable String imageId, @Valid @RequestBody EventDTO event) {
		imageService.addEvent(imageId, event);
	}
}
