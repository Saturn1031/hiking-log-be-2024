package com.phytoncide.hikinglog.domain.boards.repository;

import com.phytoncide.hikinglog.domain.boards.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    List<ImageEntity> findAllByBoardEntity_Bid(Integer bid);
}
