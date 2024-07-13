package com.phytoncide.hikinglog.domain.mountain.repository;

import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MountainRepository extends JpaRepository<MountainEntity, Integer> {

}
