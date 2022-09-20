package com.rviewer.skeletons.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.dao.EventRepository;
import com.rviewer.skeletons.dao.ImageRepository;
import com.rviewer.skeletons.model.Image;
import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.event.EventType;
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
		addEvent(image.getId(), new EventDTO(EventType.VIEW, LocalDateTime.now()));
		addEvent(image.getId(), new EventDTO(EventType.VIEW, LocalDateTime.now()));
		addEvent(image.getId(), new EventDTO(EventType.CLICK, LocalDateTime.now()));
		
		assertEquals(3, eventRepo.findByImage(image).size());
	}
	
	// invalid format test
	
	private void addEvent(String imageId, EventDTO event) throws JsonProcessingException, Exception {
		mockMvc
			.perform(post("/images/{imageId}/events", imageId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objMapper.writeValueAsBytes(event)))
			.andExpect(status().isNoContent());
	}
}
