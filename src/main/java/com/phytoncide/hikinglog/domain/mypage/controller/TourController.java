package com.phytoncide.hikinglog.domain.mypage.controller;

import com.phytoncide.hikinglog.domain.mypage.dto.TourDetailResponseDTO;
import com.phytoncide.hikinglog.domain.mypage.dto.TourResponseDTO;
import com.phytoncide.hikinglog.domain.mypage.dto.TourSaveRequestDTO;
import com.phytoncide.hikinglog.domain.mypage.entity.TourEntity;
import com.phytoncide.hikinglog.domain.mypage.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
public class TourController {

    @Autowired
    private TourService tourService;

    // 사용자가 만든 관광 코스를 불러오는 API
    @GetMapping("/tours/user/{userId}")
    public List<TourResponseDTO> getUserTours(@PathVariable Integer userId) {
        return tourService.getUserTours(userId);
    }

    @PostMapping("/save")
    public List<String> saveTour(@RequestBody TourSaveRequestDTO requestDTO) {
        // 서비스 메서드를 호출하여 투어 데이터 저장
        return tourService.saveTourData(
                requestDTO.getUserId(), // 사용자 ID 추가
                requestDTO.getTourTitle(),
                requestDTO.getMountainId(),
                requestDTO.getPreHikeAccomoIds(),
                requestDTO.getPreHikeRestaurantIds(),
                requestDTO.getPostHikeAccomoIds(),
                requestDTO.getPostHikeRestaurantIds(),
                requestDTO.getAccomoDetails(), // 숙박 DTO 리스트
                requestDTO.getRestaurantDetails() // 음식점 DTO 리스트
        );
    }

    @PutMapping("/update-status/{tourId}")
    public void updateTourStatus(@PathVariable Long tourId, @RequestParam TourEntity.Status status) {
        tourService.updateTourStatus(tourId, status);
    }

    // 특정 관광 코스의 상세 정보 조회
    @GetMapping("/details/{tourId}")
    public TourDetailResponseDTO getTourDetailsById(@PathVariable Integer tourId) {
        return tourService.getTourDetailsById(tourId);
    }
}
