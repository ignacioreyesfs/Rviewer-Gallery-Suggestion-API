package com.rviewer.skeletons.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import com.rviewer.skeletons.service.ImageDTO;

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
	
	@Test
	public void getImagesInOrder() throws JsonProcessingException, Exception {
		Image image1 = new Image("id1", "name1", "url1", LocalDateTime.now().minusDays(2));
		image1 = imageRepo.save(image1);
		imageStatsRepo.save(new ImageStats(image1));
		Image image2 = new Image("id2", "name2", "url2", LocalDateTime.now().minusDays(1));
		image2 = imageRepo.save(image2);
		imageStatsRepo.save(new ImageStats(image2));
		Image image3 = new Image("id3", "name3", "url3", LocalDateTime.now());
		image3 = imageRepo.save(image3);
		imageStatsRepo.save(new ImageStats(image3));
		Image image4 = new Image("id4", "name4", "url4", LocalDateTime.now());
		image4 = imageRepo.save(image4);
		imageStatsRepo.save(new ImageStats(image4));
		Image image5 = new Image("id5", "name5", "url5", LocalDateTime.now());
		image5 = imageRepo.save(image5);
		imageStatsRepo.save(new ImageStats(image5));
		
		addEvent(image1.getId(), new EventDTO(EventType.VIEW, LocalDateTime.now()))
				.andExpect(status().isNoContent());
		addEvent(image1.getId(), new EventDTO(EventType.VIEW, LocalDateTime.now()))
				.andExpect(status().isNoContent());
		addEvent(image4.getId(), new EventDTO(EventType.VIEW, LocalDateTime.now()))
				.andExpect(status().isNoContent());
		addEvent(image5.getId(), new EventDTO(EventType.CLICK, LocalDateTime.now()))
				.andExpect(status().isNoContent());
		
		MvcResult result = mockMvc.perform(get("/images")).andExpect(status().isOk())
				.andReturn();
		List<ImageDTO> images = objMapper.readValue(result.getResponse().getContentAsByteArray(),
				new TypeReference<List<ImageDTO>>(){});
		Logger.getGlobal().info("images: " + images);
		assertEquals(5, images.size());
		assertEquals(image5.getId(), images.get(0).getId());
		assertEquals(image1.getId(), images.get(1).getId());
		assertEquals(image4.getId(), images.get(2).getId());
		assertEquals(image3.getId(), images.get(3).getId());
		assertEquals(image2.getId(), images.get(4).getId());
	}
}
