package com.phytoncide.hikinglog.domain.mypage.service;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.mypage.dto.TourResponseDTO;
import com.phytoncide.hikinglog.domain.mypage.dto.TourSaveRequestDTO;
import com.phytoncide.hikinglog.domain.mypage.entity.TourEntity;
import com.phytoncide.hikinglog.domain.mypage.repository.TourRepository;
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
    private TourRepository tourRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 사용자가 만든 관광 코스를 불러오는 메서드
    public List<TourResponseDTO> getUserTours(Integer userId) {
        // 예시: 사용자 ID를 기준으로 투어 데이터를 조회합니다.
        List<TourEntity> tours = tourRepository.findByUserId(userId);

        // 투어 엔티티를 DTO로 변환하여 반환
        return tours.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // TourEntity를 TourResponseDTO로 변환하는 메서드
    private TourResponseDTO convertToDTO(TourEntity tour) {
        return TourResponseDTO.builder()
                .tourId(Math.toIntExact(tour.getTourId()))
                .tourTitle(tour.getTourTitle())
                .mountainId(tour.getMountainId())
                .preHikeAccomoIds(tour.getPreHikeAccomoIds())
                .preHikeRestaurantIds(tour.getPreHikeRestaurantIds())
                .postHikeAccomoIds(tour.getPostHikeAccomoIds())
                .postHikeRestaurantIds(tour.getPostHikeRestaurantIds())
                .userId(tour.getUserId())
                .status(tour.getStatus())
                .build();
    }

    // 투어 데이터를 저장하는 메서드
    public List<String> saveTourData(Integer userId,
                                     String tourTitle,
                                     Integer mountainId,
                                     List<String> preHikeAccomoIds,
                                     List<String> preHikeRestaurantIds,
                                     List<String> postHikeAccomoIds,
                                     List<String> postHikeRestaurantIds,
                                     List<AccomoDetailResponseDTO> accomoDetails,
                                     List<RestaurantDetailResponseDTO> restaurantDetails) {

        List<String> savedIds = new ArrayList<>();

        // 사용자 정보 조회
        MemberEntity user = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 투어 엔티티 생성 및 사용자 설정
        TourEntity tour = TourEntity.builder()
                .tourTitle(tourTitle)
                .mountainId(mountainId)
                .preHikeAccomoIds(preHikeAccomoIds)
                .preHikeRestaurantIds(preHikeRestaurantIds)
                .postHikeAccomoIds(postHikeAccomoIds)
                .postHikeRestaurantIds(postHikeRestaurantIds)
                .userId(user.getUid()) // 사용자 설정
                .status(TourEntity.Status.PREPARING) // 기본 상태로 예정 설정
                .build();

        // 투어 데이터 저장
        tourRepository.save(tour);

        // 투어 및 산 정보 저장
        savedIds.add(tourTitle);
        savedIds.add(String.valueOf(mountainId));

        // 숙박 및 음식점 정보를 처리하고 저장
        processAndSaveStoreData(preHikeAccomoIds, accomoDetails, "Pre-Hike Accomo ID: ", savedIds);
        processAndSaveStoreData(postHikeAccomoIds, accomoDetails, "Post-Hike Accomo ID: ", savedIds);
        processAndSaveStoreData(preHikeRestaurantIds, restaurantDetails, "Pre-Hike Restaurant ID: ", savedIds);
        processAndSaveStoreData(postHikeRestaurantIds, restaurantDetails, "Post-Hike Restaurant ID: ", savedIds);

        return savedIds;
    }

    // 숙박 및 음식점 데이터를 처리하고 저장하는 공통 메서드
    private <T> void processAndSaveStoreData(List<String> ids, List<T> details, String idPrefix, List<String> savedIds) {
        List<String> newIds = ids.stream()
                .filter(id -> !storeRepository.existsByContentId(Integer.valueOf(id)))
                .collect(Collectors.toList());
        if (!newIds.isEmpty()) {
            for (String id : newIds) {
                StoreEntity store = null;
                if (details.get(0) instanceof AccomoDetailResponseDTO) {
                    AccomoDetailResponseDTO detail = findAccomoDetailById(id, (List<AccomoDetailResponseDTO>) details);
                    if (detail != null) {
                        store = new StoreEntity(detail);
                    }
                } else if (details.get(0) instanceof RestaurantDetailResponseDTO) {
                    RestaurantDetailResponseDTO detail = findRestaurantDetailById(id, (List<RestaurantDetailResponseDTO>) details);
                    if (detail != null) {
                        store = new StoreEntity(detail);
                    }
                }
                if (store != null) {
                    storeRepository.save(store);
                } else {
                    // 로그 또는 에러 처리 추가
                    System.err.println("Detail not found for ID: " + id);
                }
            }
            savedIds.addAll(newIds.stream().map(id -> idPrefix + id).collect(Collectors.toList()));
        }
    }

    // 숙박 DTO를 contentId로 찾는 메서드
    private AccomoDetailResponseDTO findAccomoDetailById(String id, List<AccomoDetailResponseDTO> accomoDetails) {
        return accomoDetails.stream()
                .filter(dto -> dto.getContentId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 음식점 DTO를 contentId로 찾는 메서드
    private RestaurantDetailResponseDTO findRestaurantDetailById(String id, List<RestaurantDetailResponseDTO> restaurantDetails) {
        return restaurantDetails.stream()
                .filter(dto -> dto.getContentId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 관광 코스 상태 변경 메서드
    public void updateTourStatus(Long tourId, TourEntity.Status newStatus) {
        TourEntity tour = tourRepository.findById(Math.toIntExact(tourId))
                .orElseThrow(() -> new IllegalArgumentException("관광 코스를 찾을 수 없습니다."));
        tour.setStatus(newStatus);
        tourRepository.save(tour);
    }

}
