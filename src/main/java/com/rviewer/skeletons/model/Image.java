package com.rviewer.skeletons.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class Image {
	@Id
	private String id;
	private String name;
	private String url;
	private LocalDateTime createdAt;
}
