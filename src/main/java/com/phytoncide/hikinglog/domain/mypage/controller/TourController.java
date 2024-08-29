package com.phytoncide.hikinglog.domain.mypage.controller;

import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mypage.dto.TourSaveRequestDTO;
import com.phytoncide.hikinglog.domain.mypage.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
public class TourController {

    @Autowired
    private TourService tourService;

    @PostMapping("/save")
    public List<String> saveTour(@RequestBody TourSaveRequestDTO requestDTO) {
        return tourService.saveTourData(
                requestDTO.getTourTitle(),
                requestDTO.getMountainId(),
                requestDTO.getPreHikeAccomoIds(),
                requestDTO.getPreHikeRestaurantIds(),
                requestDTO.getPostHikeAccomoIds(),
                requestDTO.getPostHikeRestaurantIds()
        );
    }
}
