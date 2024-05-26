package com.phytoncide.hikinglog.domain.boards.entity;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lid")
    private Integer lid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "bid")
    private BoardEntity boardEntity;

}
