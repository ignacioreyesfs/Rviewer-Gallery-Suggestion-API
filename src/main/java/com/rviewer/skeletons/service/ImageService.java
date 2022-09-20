package com.rviewer.skeletons.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rviewer.skeletons.dao.EventRepository;
import com.rviewer.skeletons.dao.ImageRepository;
import com.rviewer.skeletons.dao.ImageStatsRepository;
import com.rviewer.skeletons.exceptions.APIError;
import com.rviewer.skeletons.exceptions.ResourceNotFoundException;
import com.rviewer.skeletons.mapper.EventMapper;
import com.rviewer.skeletons.mapper.ImageMapper;
import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.image.Image;
import com.rviewer.skeletons.model.image.ImageStats;
import com.rviewer.skeletons.model.weight.WeightCalculator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ImageService {
	private ImageRepository imageRepo;
	private EventMapper eventMapper;
	private EventRepository eventRepo;
	private ImageStatsRepository imageStatsRepo;
	private WeightCalculator weightCalculator;
	private ImageMapper imageMapper;
	
	public void saveAll(List<Image> images) {
		for(Image image: images) {
			image = imageRepo.save(image);
			imageStatsRepo.save(new ImageStats(image));
		}
	}

	public void addEvent(String imageId, EventDTO eventDTO) {
		Image image = imageRepo.findById(imageId)
				.orElseThrow(()-> new ResourceNotFoundException(APIError.IMAGE_NOT_FOUND));
		Event event = eventMapper.fromEventDTO(eventDTO);
		event.setImage(image);
		eventRepo.save(event);
		addEventToImageStats(image, event);
	}
	
	private void addEventToImageStats(Image image, Event event) {
		ImageStats imageStats = imageStatsRepo.findByImage(image).orElse(new ImageStats(image));
		imageStats.addEvent(event);
		imageStats.setWeight(weightCalculator.compute(imageStats.getEventsRepetition()));
		imageStatsRepo.save(imageStats);
	}

	public List<ImageDTO> retrieveImagesInOrder() {
		List<ImageStats> imagesStats = imageStatsRepo
				.findAll(Sort.by("weight").descending().and(Sort.by("imageCreatedAt").descending()));
		List<ImageDTO> imagesDTO = new ArrayList<>();
		for(int i = 0; i < imagesStats.size(); i++) {
			ImageDTO imageDTO = imageMapper.toImageDTO(imagesStats.get(i));
			imageDTO.setGridPosition(i+1L);
			imagesDTO.add(imageDTO);
		}
		return imagesDTO;
	}
}
