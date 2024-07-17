package com.phytoncide.hikinglog.domain.bookmarks.entity;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.store.entity.OnlineOutdoorMallEntity;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import com.phytoncide.hikinglog.global.common.BaseEntity;
import com.phytoncide.hikinglog.global.enums.BookmarkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bmarkid")
    private Integer bmarkid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "mid")
    private MountainEntity mountainEntity;

    @ManyToOne
    @JoinColumn(name = "sid")
    private StoreEntity storeEntity;

    @ManyToOne
    @JoinColumn(name = "oid")
    private OnlineOutdoorMallEntity onlineOutdoorMallEntity;

    @Enumerated(EnumType.STRING)
    private BookmarkType bookmarkType;

}

