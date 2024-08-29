package com.phytoncide.hikinglog.domain.mypage.service;

import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import com.phytoncide.hikinglog.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {
    @Autowired
    private MountainRepository mountainRepository;

    @Autowired
    private StoreRepository storeRepository;

    public List<String> saveTourData(
            String tourTitle,
            Integer mountainId,
            List<String> preHikeAccomoIds,
            List<String> preHikeRestaurantIds,
            List<String> postHikeAccomoIds,
            List<String> postHikeRestaurantIds) {

        List<String> savedIds = new ArrayList<>();

        savedIds.add(tourTitle);
        savedIds.add(String.valueOf(mountainId));

        // 등산 전 숙박 ID 처리
        List<String> newPreHikeAccomoIds = preHikeAccomoIds.stream()
                .filter(id -> !storeRepository.existsByContentId(Integer.valueOf(id)))
                .collect(Collectors.toList());
        if (!newPreHikeAccomoIds.isEmpty()) {
            // 등산 전 숙박 데이터 저장 로직이 필요하면 여기에 추가
//            StoreEntity store = StoreEntity.builder()
//                    .contentId(newPreHikeAccomoIds)
//                    .build();
//            storeRepository.save(store);

            savedIds.addAll(newPreHikeAccomoIds.stream()
                    .map(id -> "Pre-Hike Accomo ID: " + id)
                    .collect(Collectors.toList()));
        }

        // 등산 후 숙박 ID 처리
        List<String> newPostHikeAccomoIds = postHikeAccomoIds.stream()
                .filter(id -> !storeRepository.existsByContentId(Integer.valueOf(id)))
                .collect(Collectors.toList());
        if (!newPostHikeAccomoIds.isEmpty()) {
            // 등산 후 숙박 데이터 저장 로직이 필요하면 여기에 추가
            savedIds.addAll(newPostHikeAccomoIds.stream()
                    .map(id -> "Post-Hike Accomo ID: " + id)
                    .collect(Collectors.toList()));
        }

        // 등산 전 음식점 ID 처리
        List<String> newPreHikeRestaurantIds = preHikeRestaurantIds.stream()
                .filter(id -> !storeRepository.existsByContentId(Integer.valueOf(id)))
                .collect(Collectors.toList());
        if (!newPreHikeRestaurantIds.isEmpty()) {
            // 등산 전 음식점 데이터 저장 로직이 필요하면 여기에 추가
            savedIds.addAll(newPreHikeRestaurantIds.stream()
                    .map(id -> "Pre-Hike Restaurant ID: " + id)
                    .collect(Collectors.toList()));
        }

        // 등산 후 음식점 ID 처리
        List<String> newPostHikeRestaurantIds = postHikeRestaurantIds.stream()
                .filter(id -> !storeRepository.existsByContentId(Integer.valueOf(id)))
                .collect(Collectors.toList());
        if (!newPostHikeRestaurantIds.isEmpty()) {
            // 등산 후 음식점 데이터 저장 로직이 필요하면 여기에 추가
            savedIds.addAll(newPostHikeRestaurantIds.stream()
                    .map(id -> "Post-Hike Restaurant ID: " + id)
                    .collect(Collectors.toList()));
        }

        return savedIds;
    }

}
