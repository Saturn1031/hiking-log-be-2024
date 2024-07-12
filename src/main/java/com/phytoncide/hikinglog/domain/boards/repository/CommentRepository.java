package com.phytoncide.hikinglog.domain.boards.repository;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.entity.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Long countByBoardEntity_Bid(Integer bid);
    @Query("SELECT p FROM CommentEntity p WHERE p.id < :cursor AND p.boardEntity = :bid ORDER BY p.id DESC")
    List<CommentEntity> findNextPage(@Param("cursor") Integer cursor, Pageable pageable, @Param("bid") BoardEntity bid);
}
