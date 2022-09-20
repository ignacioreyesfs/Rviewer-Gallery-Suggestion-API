package com.rviewer.skeletons.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.rviewer.skeletons.dao.ImageRepository;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ImageControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ImageRepository imageRepo;
	
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
}
