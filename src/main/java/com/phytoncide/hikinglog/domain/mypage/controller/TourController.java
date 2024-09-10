package com.phytoncide.hikinglog.domain.mypage.controller;

import com.phytoncide.hikinglog.domain.mypage.dto.TourDetailResponseDTO;
import com.phytoncide.hikinglog.domain.mypage.dto.TourResponseDTO;
import com.phytoncide.hikinglog.domain.mypage.dto.TourSaveRequestDTO;
import com.phytoncide.hikinglog.domain.mypage.entity.TourEntity;
import com.phytoncide.hikinglog.domain.mypage.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
                requestDTO.getMountainName(),
                requestDTO.getPreHikeTourIds(),
                requestDTO.getPreHikeRestaurantIds(),
                requestDTO.getPostHikeTourIds(),
                requestDTO.getPostHikeRestaurantIds(),
                requestDTO.getTourDetails(), // 관광 DTO 리스트
                requestDTO.getRestaurantDetails() // 음식점 DTO 리스트
        );
    }

//    @PutMapping("/update-status/{tourId}")
//    public void updateTourStatus(@PathVariable Long tourId, @RequestParam TourEntity.Status status) {
//        tourService.updateTourStatus(tourId, status);
//    }

    @PutMapping("/update-status/{tourId}")
    public ResponseEntity<TourEntity.Status> updateTourStatus(
            @PathVariable Long tourId,
            @RequestParam TourEntity.Status status) {

        // 서비스 호출하여 상태 업데이트
        tourService.updateTourStatus(tourId, status);

        // 업데이트된 상태값을 반환
        return ResponseEntity.ok(status);
    }


    // 특정 관광 코스의 상세 정보 조회
    @GetMapping("/details/{tourId}")
    public TourDetailResponseDTO getTourDetailsById(@PathVariable Integer tourId) {
        return tourService.getTourDetailsById(tourId);
    }

    // 특정 마이관광 삭제 메서드
    @DeleteMapping("/delete/{tourId}")
    public ResponseEntity<String> deleteTour(@PathVariable Integer tourId) {
        try {
            // 투어 삭제
            tourService.deleteTourById(tourId);
            // 성공 시 응답 반환
            return ResponseEntity.ok("Tour deleted successfully.");
        } catch (IllegalArgumentException e) {
            // 예외 처리: 투어를 찾지 못한 경우
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 예외 처리: 다른 예외 발생 시
            return ResponseEntity.status(500).body("An error occurred while deleting the tour.");
        }
    }
}
