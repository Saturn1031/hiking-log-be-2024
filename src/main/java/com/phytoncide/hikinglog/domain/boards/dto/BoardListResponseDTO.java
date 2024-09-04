package com.phytoncide.hikinglog.domain.boards.dto;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private String createdAt;
        private String updatedAt;
        private Integer id;
        private String content;
        private String image;
        private String tag;
        private Integer likeNum;
        private boolean liked;
        private Integer commentNum;
        private Integer userid;
        private String userName;
        private String userImage;


        public BoardResponseDTO(BoardEntity boardEntity) {
            this.id = boardEntity.getBid();
            this.content = boardEntity.getContent();
            this.userid = boardEntity.getMemberEntity().getUid();
            this.userName = boardEntity.getMemberEntity().getName();
            this.userImage = boardEntity.getMemberEntity().getImage();
        }


        public static BoardResponseDTO toDTO(BoardEntity entity,
                                             String image,
                                             Integer likeNum,
                                             boolean liked,
                                             Integer commentNum) {
            return BoardResponseDTO.builder()
                    .createdAt(entity.getCreatedAt().toString())
                    .updatedAt(entity.getUpdatedAt().toString())
                    .id(entity.getBid())
                    .content(entity.getContent())
                    .image(image)
                    .tag(entity.getTag())
                    .likeNum(likeNum)
                    .liked(liked)
                    .commentNum(commentNum)
                    .userid(entity.getMemberEntity().getUid())
                    .userName(entity.getMemberEntity().getName())
                    .userImage(entity.getMemberEntity().getImage())
                    .build();
        }
    }
}
