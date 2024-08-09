package com.phytoncide.hikinglog.domain.mountain.repository;

import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MountainRepository extends JpaRepository<MountainEntity, Integer> {
    MountainEntity findByMntilistno(Integer mntilistno);
    boolean existsByMntilistno(Integer mnlistno);
}
