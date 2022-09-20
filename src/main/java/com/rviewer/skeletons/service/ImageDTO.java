package com.rviewer.skeletons.service;

import java.util.Map;

import com.rviewer.skeletons.model.event.EventType;

import lombok.Data;

@Data
public class ImageDTO {
	private String id;
	private String name;
	private String url;
	private double weight;
	private Long gridPosition;
	private Map<EventType, Long> events;
}
