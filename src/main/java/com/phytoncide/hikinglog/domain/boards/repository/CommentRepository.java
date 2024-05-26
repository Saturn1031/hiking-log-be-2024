package com.phytoncide.hikinglog.domain.boards.repository;

import com.phytoncide.hikinglog.domain.boards.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByBoardEntity_Bid(Integer bid);
    boolean existsByBoardEntity_Bid(Integer bid);
    Long countByBoardEntity_Bid(Integer bid);
    List<CommentEntity> findTop1ByOrderByCreatedAtDesc();
}
