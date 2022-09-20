package com.rviewer.skeletons.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.event.EventType;
import com.rviewer.skeletons.model.image.Image;
import com.rviewer.skeletons.model.weight.WeightAdderCalculator;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:event-weight.properties")
public class WeightCalculationTest {
	@Autowired
	private WeightAdderCalculator weightCalculator;
	@Autowired
	private Environment env;
	
	@Test
	public void weightAddingEventListTest() {
		Image image = new Image("id", "name", "url", LocalDateTime.now());
		List<Event> twoEvents = Arrays.asList(new Event(EventType.VIEW, LocalDateTime.now(), image), 
				new Event(EventType.VIEW, LocalDateTime.now().plusDays(2), image));
		List<Event> threeEvents = Arrays.asList(new Event(EventType.VIEW, LocalDateTime.now(), image), 
				new Event(EventType.VIEW, LocalDateTime.now().plusDays(2), image),
				new Event(EventType.VIEW, LocalDateTime.now().plusDays(3), image));
		
		assertTrue(weightCalculator.compute(twoEvents) < weightCalculator.compute(threeEvents));
		assertEquals(Double.parseDouble(env.getProperty(EventType.VIEW.name(), "0"))*3, weightCalculator.compute(threeEvents));
	}
	
	@Test
	public void weightAddingStatsTest() {
		Map<EventType, Long> fewerEventsRepets = new EnumMap<>(EventType.class);
		fewerEventsRepets.put(EventType.VIEW, 5L);
		Map<EventType, Long> moreEventsRepets = new EnumMap<>(EventType.class);
		moreEventsRepets.put(EventType.VIEW, 10L);
		assertTrue(weightCalculator.compute(fewerEventsRepets) < weightCalculator.compute(moreEventsRepets));
		assertEquals(Double.parseDouble(env.getProperty(EventType.VIEW.name(), "0"))*5, weightCalculator.compute(fewerEventsRepets));		
	}
}
