package com.phytoncide.hikinglog.domain.boards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iid")
    private Integer iid;

    @Column(name = "originalImageName", nullable = false, length = 100)
    private String originalImageName;

    @Column(name = "storedFileName", nullable = false, length = 100)
    private String storedFileName;

    @ManyToOne
    @JoinColumn(name="bid")
    private BoardEntity boardEntity;
}
