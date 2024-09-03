package com.phytoncide.hikinglog.domain.mypage.dto;

import com.phytoncide.hikinglog.domain.mypage.entity.TourEntity;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TourDetailResponseDTO {
    private Integer tourId;
    private String tourTitle;
    private Integer mountainId;
    private List<StoreEntity> preHikeAccomo;
    private List<StoreEntity> preHikeRestaurant;
    private List<StoreEntity> postHikeAccomo;
    private List<StoreEntity> postHikeRestaurant;
    private Enum<TourEntity.Status> status; // 'PREPARING' 또는 'COMPLETED'
}
