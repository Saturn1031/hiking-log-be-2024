package com.phytoncide.hikinglog.domain.boards.repository;

import com.phytoncide.hikinglog.domain.boards.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

}
