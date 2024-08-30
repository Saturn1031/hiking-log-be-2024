package com.phytoncide.hikinglog.domain.mypage.repository;

import com.phytoncide.hikinglog.domain.mypage.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<TourEntity, Integer> {

    // 특정 사용자가 만든 투어를 조회하는 메서드
    List<TourEntity> findByUserId(Integer userId);
}