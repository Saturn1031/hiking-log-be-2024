package com.phytoncide.hikinglog.domain.bookmarks.dto;

import com.phytoncide.hikinglog.domain.bookmarks.entity.BookmarkEntity;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.global.enums.BookmarkType;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreBookmarkCreateDTO {

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private String phone;

    private String image;

    public StoreEntity toStoreEntity(Integer storeId) {
        StoreEntity storeEntity = StoreEntity.builder()
                .contentId(storeId)
                .sName(name)
                .location(location)
                .phone(phone)
                .sImage(image)
                .build();

        return storeEntity;
    }

    public BookmarkEntity toBookmarkEntity(MemberEntity memberEntity, StoreEntity storeEntity, BookmarkType bookmarkType) {
        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .memberEntity(memberEntity)
                .storeEntity(storeEntity)
                .bookmarkType(bookmarkType)
                .build();

        return bookmarkEntity;
    }

}
