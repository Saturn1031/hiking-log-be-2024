package com.phytoncide.hikinglog.domain.boards.repository;

import com.phytoncide.hikinglog.domain.boards.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<LikesEntity, Integer> {
    List<LikesEntity> findAllByBoardEntity_Bid(Integer bid);
    boolean existsByBoardEntity_BidAndMemberEntity_Uid(Integer bid, Integer uid);
    Long countByBoardEntity_Bid(Integer bid);
}
