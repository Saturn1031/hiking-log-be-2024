package com.phytoncide.hikinglog.domain.record.repository;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {

    long countByMidAndUid(MountainEntity mid, MemberEntity uid);
    List<RecordEntity> findByUid(MemberEntity uid);
}
