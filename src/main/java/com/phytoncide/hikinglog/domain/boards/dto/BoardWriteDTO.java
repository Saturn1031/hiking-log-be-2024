package com.phytoncide.hikinglog.domain.boards.dto;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.entity.ImageEntity;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteDTO {

    @NotNull
    private String title;

    @NotNull
    private String content;

    private String tag;

    public BoardEntity toBoardEntity(MemberEntity memberEntity) {
        BoardEntity board = BoardEntity.builder()
                .memberEntity(memberEntity)
                .title(title)
                .content(content)
                .tag(tag)
                .build();

        return board;
    }

    public ImageEntity toImageEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        ImageEntity image = ImageEntity.builder()
                .boardEntity(boardEntity)
                .originalImageName(originalFileName)
                .storedFileName(storedFileName)
                .build();

        return image;
    }
}
