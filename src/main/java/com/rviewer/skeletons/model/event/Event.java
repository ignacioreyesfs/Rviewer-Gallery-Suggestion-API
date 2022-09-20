package com.rviewer.skeletons.model.event;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rviewer.skeletons.model.Image;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="event")
@NoArgsConstructor
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private EventType type;
	private LocalDateTime timestamp;
	@ManyToOne
	@JoinColumn(name="image_id")
	private Image image;
}
