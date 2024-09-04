package com.phytoncide.hikinglog.domain.boards.dto;

import com.phytoncide.hikinglog.domain.boards.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
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
        private String createdAt;
        private Integer id;
        private String content;
        private Integer userid;
        private String userName;
        private String userImage;

        public static CommentResponseDTO toDTO(CommentEntity entity) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

            return CommentResponseDTO.builder()
                    .createdAt(entity.getCreatedAt().format(dateTimeFormatter))
                    .id(entity.getCid())
                    .content(entity.getContent())
                    .userid(entity.getMemberEntity().getUid())
                    .userName(entity.getMemberEntity().getName())
                    .userImage(entity.getMemberEntity().getImage())
                    .build();
        }
    }
}
