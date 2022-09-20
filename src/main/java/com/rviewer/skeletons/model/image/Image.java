package com.rviewer.skeletons.model.image;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {
	@Id
	private String id;
	private String name;
	private String url;
	private LocalDateTime createdAt;
}
