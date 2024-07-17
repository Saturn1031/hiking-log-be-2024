package com.phytoncide.hikinglog.domain.bookmarks.dto;

import com.phytoncide.hikinglog.domain.bookmarks.entity.BookmarkEntity;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
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
public class MountainBookmarkCreateDTO {

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private String info;

    @NotNull
    private Double high;

    private String image;

    public MountainEntity toMountainEntity(Integer mntilistno) {
        MountainEntity mountainEntity = MountainEntity.builder()
                .mntilistno(mntilistno)
                .mName(name)
                .location(location)
                .info(info)
                .mntiHigh(high)
                .mImage(image)
                .build();

        return mountainEntity;
    }

    public BookmarkEntity toBookmarkEntity(MemberEntity memberEntity, MountainEntity mountainEntity, BookmarkType bookmarkType) {
        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .memberEntity(memberEntity)
                .mountainEntity(mountainEntity)
                .bookmarkType(bookmarkType)
                .build();

        return bookmarkEntity;
    }

}
