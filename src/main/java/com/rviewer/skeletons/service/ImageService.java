package com.rviewer.skeletons.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rviewer.skeletons.dao.EventRepository;
import com.rviewer.skeletons.dao.ImageRepository;
import com.rviewer.skeletons.exceptions.APIError;
import com.rviewer.skeletons.exceptions.ResourceNotFoundException;
import com.rviewer.skeletons.mapper.EventMapper;
import com.rviewer.skeletons.model.Image;
import com.rviewer.skeletons.model.event.Event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ImageService {
	private ImageRepository imageRepo;
	private EventMapper eventMapper;
	private EventRepository eventRepo;
	
	public void saveAll(List<Image> images) {
		imageRepo.saveAll(images);
	}

	public void addEvent(String imageId, EventDTO eventDTO) {
		Image image = imageRepo.findById(imageId)
				.orElseThrow(()-> new ResourceNotFoundException(APIError.IMAGE_NOT_FOUND));
		Event event = eventMapper.fromEventDTO(eventDTO);
		event.setImage(image);
		eventRepo.save(event);
	}
}
