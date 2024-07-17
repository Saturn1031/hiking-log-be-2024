package com.phytoncide.hikinglog.domain.store.entity;

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
@Table(name = "onlineOutdoorMall")
public class OnlineOutdoorMallEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "storeId")
    private Integer storeId;

    @Column(nullable = false)
    private String oName;

    @Column(nullable = false)
    private String link;

    @Column
    private String oImage;

}
