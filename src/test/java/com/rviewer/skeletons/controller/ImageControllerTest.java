package com.rviewer.skeletons.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.dao.EventRepository;
import com.rviewer.skeletons.dao.ImageRepository;
import com.rviewer.skeletons.dao.ImageStatsRepository;
import com.rviewer.skeletons.mapper.EventMapper;
import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.event.EventType;
import com.rviewer.skeletons.model.image.Image;
import com.rviewer.skeletons.model.image.ImageStats;
import com.rviewer.skeletons.model.weight.WeightCalculator;
import com.rviewer.skeletons.service.EventDTO;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ImageControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ImageRepository imageRepo;
	@Autowired
	private ObjectMapper objMapper;
	@Autowired
	private EventRepository eventRepo;
	@Autowired
	private ImageStatsRepository imageStatsRepo;
	@Autowired
	private WeightCalculator weigthCalculator;
	@Autowired
	private EventMapper eventMapper;
	
	@Test
	public void loadImageSetTest() throws Exception {
		String dataset = IOUtils.toString(this.getClass().getClassLoader()
				.getResourceAsStream("data.json"), "UTF-8");
		mockMvc.perform(post("/images")
				.contentType(MediaType.APPLICATION_JSON)
				.content(dataset))
				.andExpect(status().isNoContent());
		assertEquals(1000, imageRepo.count());
	}
	
	@Test
	public void addEventTest() throws JsonProcessingException, Exception {
		Image image = new Image("id1", "name1", "url1", LocalDateTime.now());
		image = imageRepo.save(image);
		addEvent(image.getId(), new EventDTO(EventType.VIEW, LocalDateTime.now())).andExpect(status().isNoContent());
		addEvent(image.getId(), new EventDTO(EventType.VIEW, LocalDateTime.now())).andExpect(status().isNoContent());
		addEvent(image.getId(), new EventDTO(EventType.CLICK, LocalDateTime.now())).andExpect(status().isNoContent());
		
		assertEquals(3, eventRepo.findByImage(image).size());
	}
	
	@Test
	public void addEventImageStatsTest() throws JsonProcessingException, Exception {
		Image image = new Image("id1", "name1", "url1", LocalDateTime.now());
		image = imageRepo.save(image);
		ImageStats imageStats;
		List<EventDTO> eventsDTO = Arrays.asList(new EventDTO(EventType.VIEW, LocalDateTime.now()),
				new EventDTO(EventType.VIEW, LocalDateTime.now()), new EventDTO(EventType.CLICK, LocalDateTime.now()));
		for(EventDTO event: eventsDTO) {
			addEvent(image.getId(), event).andExpect(status().isNoContent());
		}
		
		imageStats = imageStatsRepo.findByImage(image).orElse(null);
		assertNotNull(imageStats);
		assertEquals(2, imageStats.getEventsRepetition().get(EventType.VIEW));
		assertEquals(1, imageStats.getEventsRepetition().get(EventType.CLICK));
		List<Event> events = eventsDTO.stream().map(event -> eventMapper.fromEventDTO(event)).toList();
		assertEquals(weigthCalculator.compute(events), imageStats.getWeight());
	}
	
	@Test
	public void addInvalidEventTest() throws JsonProcessingException, Exception {
		Image image = new Image("id1", "name1", "url1", LocalDateTime.now());
		image = imageRepo.save(image);
		addEvent(image.getId(), new EventDTO(EventType.VIEW, null)).andExpect(status().isBadRequest());
		assertEquals(0, eventRepo.findByImage(image).size());
	}
	
	private ResultActions addEvent(String imageId, EventDTO event) throws JsonProcessingException, Exception {
		return mockMvc
			.perform(post("/images/{imageId}/events", imageId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objMapper.writeValueAsBytes(event)));
	}
}
