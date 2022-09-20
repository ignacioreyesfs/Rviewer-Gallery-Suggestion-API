package com.rviewer.skeletons.model.weight;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.event.EventType;

@Component
@PropertySource("classpath:event-weight.properties")
public class WeightAdderCalculator implements WeightCalculator{
	@Autowired
	Environment env;

	@Override
	public double compute(List<Event> events) {
		return events.stream()
				.mapToDouble(event -> Double.parseDouble(env.getProperty(event.getType().name(), "0")))
				.reduce(0, Double::sum);
	}

	@Override
	public double compute(Map<EventType, Long> eventsRepetition) {
		return eventsRepetition.entrySet().stream()
				.mapToDouble(entry -> Double.parseDouble(env.getProperty(entry.getKey().name(), "0"))*entry.getValue())
				.reduce(0, Double::sum);
	}
	
}
