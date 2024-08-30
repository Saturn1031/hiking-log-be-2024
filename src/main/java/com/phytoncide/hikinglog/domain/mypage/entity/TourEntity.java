package com.phytoncide.hikinglog.domain.mypage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tour")
public class TourEntity {

    public enum Status {
        PREPARING, // 예정
        COMPLETED // 완료
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourId;

    @Column(nullable = false)
    private String tourTitle;

    @Column(nullable = false)
    private Integer mountainId;

    @ElementCollection
    @Column(name = "accommodation_id")
    private List<String> preHikeAccomoIds;

    @ElementCollection
    @Column(name = "restaurant_id")
    private List<String> preHikeRestaurantIds;

    @ElementCollection
    @Column(name = "accommodation_id")
    private List<String> postHikeAccomoIds;

    @ElementCollection
    @Column(name = "restaurant_id")
    private List<String> postHikeRestaurantIds;

    @Column(nullable = false)
    private Integer userId; // 사용자의 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PREPARING; // 기본값으로 예정 설정
}
