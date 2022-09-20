package com.rviewer.skeletons.mapper;

import org.springframework.stereotype.Component;

import com.rviewer.skeletons.model.image.Image;
import com.rviewer.skeletons.model.image.ImageStats;
import com.rviewer.skeletons.service.ImageDTO;

@Component
public class ImageMapper {
	public ImageDTO toImageDTO(ImageStats imageStats) {
		ImageDTO imageDTO = new ImageDTO();
		Image image = imageStats.getImage();
		
		imageDTO.setId(image.getId());
		imageDTO.setName(image.getName());
		imageDTO.setUrl(image.getUrl());
		imageDTO.setWeight(imageStats.getWeight());
		imageDTO.setEvents(imageStats.getEventsRepetition());
		
		return imageDTO;
	}
}
