package com.phytoncide.hikinglog.domain.boards.dto;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListResponseDTO {

    private List<CommentResponseDTO> commentList;
    private boolean hasNext;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResponseDTO {
        private LocalDateTime createdAt;
        private Integer id;
        private String content;
        private Integer userid;

        public static CommentResponseDTO toDTO(CommentEntity entity) {
            return CommentResponseDTO.builder()
                    .createdAt(entity.getCreatedAt())
                    .id(entity.getCid())
                    .content(entity.getContent())
                    .userid(entity.getMemberEntity().getUid())
                    .build();
        }
    }
}
