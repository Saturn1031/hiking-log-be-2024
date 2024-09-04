package com.phytoncide.hikinglog.domain.mountain.entity;

import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.global.enums.Level;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mountain")
public class MountainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mid")
    private Integer mid;

    @Column(nullable = false)
    private Integer mntilistno;

    @Column(nullable = false)
    private String mName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 5000)
    private String info;

    @Column
    private Double mntiHigh;

    @Column
    private String mImage;

    // SaveMountainDTO를 받아들이는 생성자
    public MountainEntity(SaveMountainDTO dto) {
        this.mntilistno = dto.getMntilistno();
        this.mName = dto.getMName();
        this.location = dto.getLocation();
        this.info = dto.getInfo();
        this.mntiHigh = dto.getMntiHigh();
        this.mImage = dto.getMImage();
    }

}
