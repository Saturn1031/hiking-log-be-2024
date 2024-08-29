package com.phytoncide.hikinglog.domain.mypage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "mytours")
@Getter
@Setter
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
