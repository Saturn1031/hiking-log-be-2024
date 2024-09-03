package com.phytoncide.hikinglog.domain.record.repository;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {

    long countByMidAndUid(MountainEntity mid, MemberEntity uid);
    List<RecordEntity> findByUid(MemberEntity uid);
    Optional<RecordEntity> findByRid(Integer rid);
    List<RecordEntity> findByMidAndUidAndRidGreaterThan(MountainEntity mid, MemberEntity uid, Integer rid);
}
