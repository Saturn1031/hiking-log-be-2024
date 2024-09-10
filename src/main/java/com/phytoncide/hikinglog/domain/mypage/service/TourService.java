package com.phytoncide.hikinglog.domain.mypage.service;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.mypage.dto.TourDetailResponseDTO;
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

    // 특정 관광 코스의 상세 정보 가져오기
    public TourDetailResponseDTO getTourDetailsById(Integer tourId) {
        // 관광 코스 조회
        TourEntity tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("관광 코스를 찾을 수 없습니다."));

        // 관광 및 음식점 ID 목록 가져오기
        List<StoreEntity> preHikeTour = storeRepository.findByContentIdIn(tour.getPreHikeTourIds());
        List<StoreEntity> preHikeRestaurant = storeRepository.findByContentIdIn(tour.getPreHikeRestaurantIds());
        List<StoreEntity> postHikeTour = storeRepository.findByContentIdIn(tour.getPostHikeTourIds());
        List<StoreEntity> postHikeRestaurant = storeRepository.findByContentIdIn(tour.getPostHikeRestaurantIds());

        // DTO로 변환
        return TourDetailResponseDTO.builder()
                .tourId(tourId)
                .tourTitle(tour.getTourTitle())
                .mountainId(tour.getMountainId())
                .mountainName(tour.getMountainName())
                .preHikeTour(preHikeTour)
                .preHikeRestaurant(preHikeRestaurant)
                .postHikeTour(postHikeTour)
                .postHikeRestaurant(postHikeRestaurant)
                .status(tour.getStatus())
                .build();
    }

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
                .mountainName(tour.getMountainName())
                .preHikeTourIds(tour.getPreHikeTourIds())
                .preHikeRestaurantIds(tour.getPreHikeRestaurantIds())
                .postHikeTourIds(tour.getPostHikeTourIds())
                .postHikeRestaurantIds(tour.getPostHikeRestaurantIds())
                .userId(tour.getUserId())
                .status(tour.getStatus())
                .build();
    }

    // 투어 데이터를 저장하는 메서드
    public List<String> saveTourData(Integer userId,
                                     String tourTitle,
                                     Integer mountainId,
                                     String mountainName,
                                     List<String> preHikeTourIds,
                                     List<String> preHikeRestaurantIds,
                                     List<String> postHikeTourIds,
                                     List<String> postHikeRestaurantIds,
                                     List<AccomoDetailResponseDTO> tourDetails,
                                     List<RestaurantDetailResponseDTO> restaurantDetails) {

        List<String> savedIds = new ArrayList<>();

        // 사용자 정보 조회
        MemberEntity user = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 투어 엔티티 생성 및 사용자 설정
        TourEntity tour = TourEntity.builder()
                .tourTitle(tourTitle)
                .mountainId(mountainId)
                .mountainName(mountainName)
                .preHikeTourIds(preHikeTourIds)
                .preHikeRestaurantIds(preHikeRestaurantIds)
                .postHikeTourIds(postHikeTourIds)
                .postHikeRestaurantIds(postHikeRestaurantIds)
                .userId(user.getUid()) // 사용자 설정
                .status(TourEntity.Status.PREPARING) // 기본 상태로 예정 설정
                .build();

        // 투어 데이터 저장
        TourEntity savedTour = tourRepository.save(tour);

        // 투어 및 산 정보 저장
        savedIds.add(savedTour.getTourId().toString()); // 저장된 투어의 ID 추가
        savedIds.add(tourTitle);
        savedIds.add(String.valueOf(mountainId));
        savedIds.add(mountainName);

        // 숙박 및 음식점 정보를 처리하고 저장
        processAndSaveStoreData(preHikeTourIds, tourDetails, "Pre-Hike Tour ID: ", savedIds);
        processAndSaveStoreData(postHikeTourIds, tourDetails, "Post-Hike Tour ID: ", savedIds);
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
    private AccomoDetailResponseDTO findAccomoDetailById(String id, List<AccomoDetailResponseDTO> tourDetails) {
        return tourDetails.stream()
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

    // 특정 마이관광 삭제 메서드
    public void deleteTourById(Integer tourId) {
        // 투어 존재 여부 확인
        TourEntity tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 관광 코스를 찾을 수 없습니다."));

        // 투어 삭제
        tourRepository.delete(tour);
    }

}
