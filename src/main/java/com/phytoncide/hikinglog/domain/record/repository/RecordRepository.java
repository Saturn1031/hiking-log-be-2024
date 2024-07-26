package com.phytoncide.hikinglog.domain.record.repository;

import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {
}
