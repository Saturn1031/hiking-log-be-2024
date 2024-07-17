package com.phytoncide.hikinglog.domain.store.entity;

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

}
