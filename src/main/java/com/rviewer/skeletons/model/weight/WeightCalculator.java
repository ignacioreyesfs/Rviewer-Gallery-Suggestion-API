package com.rviewer.skeletons.model.weight;

import java.util.List;
import java.util.Map;

import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.event.EventType;

public interface WeightCalculator {
	public double compute(List<Event> events);
	public double compute(Map<EventType, Long> eventsRepetition);
}
