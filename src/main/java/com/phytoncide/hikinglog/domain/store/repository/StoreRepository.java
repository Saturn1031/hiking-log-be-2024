package com.phytoncide.hikinglog.domain.store.repository;

import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
    StoreEntity findByContentId(Integer contentId);
}
