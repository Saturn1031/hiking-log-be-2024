package com.phytoncide.hikinglog.domain.bookmarks.dto;

import com.phytoncide.hikinglog.domain.bookmarks.entity.BookmarkEntity;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.store.entity.OnlineOutdoorMallEntity;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import com.phytoncide.hikinglog.global.enums.BookmarkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlinestoreBookmarkCreateDTO {

    @NotNull
    private String name;

    @NotNull
    private String link;

    private String image;

    public OnlineOutdoorMallEntity toOnlinestoreMallEntity(Integer storeId) {
        OnlineOutdoorMallEntity onlineOutdoorMallEntity = OnlineOutdoorMallEntity.builder()
                .storeId(storeId)
                .oName(name)
                .link(link)
                .oImage(image)
                .build();

        return onlineOutdoorMallEntity;
    }

    public BookmarkEntity toBookmarkEntity(MemberEntity memberEntity, OnlineOutdoorMallEntity onlineOutdoorMallEntity, BookmarkType bookmarkType) {
        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .memberEntity(memberEntity)
                .onlineOutdoorMallEntity(onlineOutdoorMallEntity)
                .bookmarkType(bookmarkType)
                .build();

        return bookmarkEntity;
    }

}
