package com.rviewer.skeletons.service;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.rviewer.skeletons.model.event.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
	@NotNull(message="required")
	private EventType type;
	@NotNull(message="required")
	private LocalDateTime timestamp;
}
