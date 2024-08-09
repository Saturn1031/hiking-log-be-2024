package com.phytoncide.hikinglog.domain.boards.repository;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.entity.NotificationEntity;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    @Query("SELECT p FROM NotificationEntity p WHERE p.id < :cursor AND p.notifiedUid = :uid ORDER BY p.id DESC")
    List<NotificationEntity> findNextPage(@Param("cursor") Integer cursor, Pageable pageable, @Param("uid") MemberEntity uid);
}
