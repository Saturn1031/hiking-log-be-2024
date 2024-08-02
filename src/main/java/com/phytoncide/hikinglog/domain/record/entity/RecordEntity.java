package com.phytoncide.hikinglog.domain.record.entity;


import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "record")
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid")
    private Integer rid;

    @ManyToOne
    private MemberEntity uid;

    @ManyToOne
    private MountainEntity mid;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private long time;

    @Column(nullable = false)
    private long number;
}
