package com.phytoncide.hikinglog.domain.mypage.dto;

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
}