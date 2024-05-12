package com.phytoncide.hikinglog.domain.mountain.entity;

import com.phytoncide.hikinglog.global.enums.Level;
import jakarta.persistence.*;
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
    @Column(name = "mountain_id")
    private Integer mountainId;

    @Column(nullable = false)
    private String mName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String info;

    @Column
    private String mImage;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(nullable = false)
    private String features;

}
