package com.rviewer.skeletons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rviewer.skeletons.model.event.Event;
import com.rviewer.skeletons.model.image.Image;

public interface EventRepository extends JpaRepository<Event, Long>{
	public List<Event> findByImage(Image image);
}
