package com.rviewer.skeletons.mapper;

import org.springframework.stereotype.Component;

import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.service.EventDTO;

@Component
public class EventMapper {
	public Event fromEventDTO(EventDTO eventDTO) {
		Event event = new Event();
		event.setType(eventDTO.getType());
		event.setTimestamp(eventDTO.getTimestamp());
		return event;
	}
}
