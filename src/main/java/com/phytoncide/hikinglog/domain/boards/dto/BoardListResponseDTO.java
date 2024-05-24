package com.phytoncide.hikinglog.domain.boards.dto;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
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
public class BoardListResponseDTO {

    private List<BoardResponseDTO> boardList;
    private boolean hasNext;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardResponseDTO {
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Integer id;
        private String title;
        private String content;
        private List<String> images;
        private String tag;
        private Integer likeNum;
        private boolean liked;
        private Integer commentNum;
        private Integer commentid;
        private Integer userid;

        public static BoardResponseDTO toDTO(BoardEntity entity,
                                             List<String> images,
                                             Integer likeNum,
                                             boolean liked,
                                             Integer commentNum,
                                             Integer commentid) {
            return BoardResponseDTO.builder()
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .id(entity.getBid())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .images(images)
                    .tag(entity.getTag())
                    .likeNum(likeNum)
                    .liked(liked)
                    .commentNum(commentNum)
                    .commentid(commentid)
                    .userid(entity.getMemberEntity().getUid())
                    .build();
        }
    }
}
