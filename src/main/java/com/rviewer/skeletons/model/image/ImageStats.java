package com.rviewer.skeletons.model.image;

import java.util.EnumMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.event.EventType;

import lombok.Data;

@Data
@Entity
@Table(name = "image_stats")
public class ImageStats { // Denormalized Image
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "image_id")
	private Image image;
	@ElementCollection
	@CollectionTable(name = "events_repetitions", joinColumns = @JoinColumn(name = "image_id"))
	@MapKeyEnumerated
	@Column(name="repetitions")
	private Map<EventType, Long> eventsRepetition;
	private double weight;
	
	public ImageStats() {
		this.eventsRepetition = new EnumMap<>(EventType.class);
	}
	
	public ImageStats(Image image) {
		this();
		this.image = image;
	}
	
	public void addEvent(Event event) {
		eventsRepetition.put(event.getType(), eventsRepetition.getOrDefault(event.getType(), 0L)+1L);
	}
}
