package com.phytoncide.hikinglog.domain.mypage.repository;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyBoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findByMemberEntityUid(Integer uid, Pageable pageable);
}
