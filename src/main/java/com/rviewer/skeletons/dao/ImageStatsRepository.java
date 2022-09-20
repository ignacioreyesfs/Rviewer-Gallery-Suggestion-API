package com.rviewer.skeletons.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rviewer.skeletons.model.image.Image;
import com.rviewer.skeletons.model.image.ImageStats;

public interface ImageStatsRepository extends JpaRepository<ImageStats, Long> {
	public Optional<ImageStats> findByImage(Image image);
}
