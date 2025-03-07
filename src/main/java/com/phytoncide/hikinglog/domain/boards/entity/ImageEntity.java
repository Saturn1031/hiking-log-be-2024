package com.phytoncide.hikinglog.domain.boards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "storedUrl", nullable = false, columnDefinition = "text")
    private String storedUrl;

    @ManyToOne
    @JoinColumn(name = "bid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BoardEntity boardEntity;

}
