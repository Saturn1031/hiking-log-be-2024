package com.phytoncide.hikinglog.domain.mypage.repository;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyBoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findByMemberEntityUid(Integer uid);

    @Query("SELECT b FROM BoardEntity b WHERE b.memberEntity.uid = :userId ORDER BY b.createdAt DESC")
    List<BoardEntity> findNextPageByUserId(@Param("userId") Integer userId, Pageable pageable);

}
