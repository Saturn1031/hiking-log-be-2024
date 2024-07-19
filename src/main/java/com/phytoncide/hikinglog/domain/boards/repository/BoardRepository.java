package com.phytoncide.hikinglog.domain.boards.repository;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    @Query("SELECT p FROM BoardEntity p WHERE p.id < :cursor ORDER BY p.id DESC")
    List<BoardEntity> findNextPage(@Param("cursor") Integer cursor, Pageable pageable);

}
