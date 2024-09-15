package com.phytoncide.hikinglog.domain.boards.entity;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "comment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @ManyToOne
    @JoinColumn(name = "uid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "bid")
    private BoardEntity boardEntity;

}
