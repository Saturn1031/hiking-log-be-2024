package com.phytoncide.hikinglog.domain.mypage.dto;

import com.phytoncide.hikinglog.domain.mypage.entity.TourEntity;
import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantDetailResponseDTO;
import lombok.Data;
import java.util.List;

@Data
public class TourSaveRequestDTO {

    private String tourTitle;
    private Integer mountainId;

    private List<String> preHikeAccomoIds;      // 등산 전 숙박 ID 리스트
    private List<String> preHikeRestaurantIds;  // 등산 전 음식점 ID 리스트
    private List<String> postHikeAccomoIds;     // 등산 후 숙박 ID 리스트
    private List<String> postHikeRestaurantIds; // 등산 후 음식점 ID 리스트

    // 추가된 DTO 리스트들
    private List<AccomoDetailResponseDTO> accomoDetails; // 숙박 정보 DTO 리스트
    private List<RestaurantDetailResponseDTO> restaurantDetails; // 음식점 정보 DTO 리스트

    // 사용자 ID 추가
    private Integer userId;

    private Enum<TourEntity.Status> status;
}