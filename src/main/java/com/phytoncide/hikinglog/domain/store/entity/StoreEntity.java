package com.phytoncide.hikinglog.domain.store.entity;

import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantDetailResponseDTO;
import com.phytoncide.hikinglog.global.enums.Level;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sid")
    private Integer sid;

    @Column(nullable = false)
    private Integer contentId;

    @Column(nullable = false)
    private String sName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String phone;

    @Column
    private String sImage;

    // AccomoDetailResponseDTO를 StoreEntity로 변환하는 생성자
    public StoreEntity(AccomoDetailResponseDTO dto) {
        this.contentId = Integer.parseInt(dto.getContentId());
        this.sName = dto.getName(); // sName에 매핑
        this.location = dto.getAdd(); // location에 주소 매핑
        this.phone = dto.getTel(); // phone에 전화번호 매핑
        this.sImage = dto.getImg(); // sImage에 이미지 매핑
    }

    // RestaurantDetailResponseDTO를 StoreEntity로 변환하는 생성자
    public StoreEntity(RestaurantDetailResponseDTO dto) {
        this.contentId = Integer.parseInt(dto.getContentId());
        this.sName = dto.getName(); // sName에 매핑
        this.location = dto.getAdd(); // location에 주소 매핑
        this.phone = dto.getTel(); // phone에 전화번호 매핑
        this.sImage = dto.getImg(); // sImage에 이미지 매핑
    }

}
