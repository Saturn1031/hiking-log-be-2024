package com.phytoncide.hikinglog.domain.store.repository;

import com.phytoncide.hikinglog.domain.store.entity.OnlineOutdoorMallEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineOutdoorMallRepository extends JpaRepository<OnlineOutdoorMallEntity, Integer> {
    OnlineOutdoorMallEntity findByStoreId(Integer storeId);
}
