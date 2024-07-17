package com.phytoncide.hikinglog.domain.bookmarks.dto;

import com.phytoncide.hikinglog.domain.bookmarks.entity.BookmarkEntity;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.store.entity.OnlineOutdoorMallEntity;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import com.phytoncide.hikinglog.global.enums.BookmarkType;
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
public class MountainBookmarkListResponseDTO {

    private List<MountainBookmarkResponseDTO> bookmarkList;
    private boolean hasNext;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MountainBookmarkResponseDTO {
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Integer id;
        private Integer mntilistno;
        private String name;
        private String image;
        private String location;
        private Double mntiHigh;
        private String mntiInfo;
        private BookmarkType bookmarkType;

        public static MountainBookmarkResponseDTO toDTO(BookmarkEntity bookmarkEntity,
                                                        MountainEntity mountainEntity) {
            return MountainBookmarkResponseDTO.builder()
                    .createdAt(bookmarkEntity.getCreatedAt())
                    .updatedAt(bookmarkEntity.getUpdatedAt())
                    .id(mountainEntity.getMid())
                    .mntilistno(mountainEntity.getMntilistno())
                    .name(mountainEntity.getMName())
                    .image(mountainEntity.getMImage())
                    .location(mountainEntity.getLocation())
                    .mntiHigh(mountainEntity.getMntiHigh())
                    .mntiInfo(mountainEntity.getInfo())
                    .bookmarkType(bookmarkEntity.getBookmarkType())
                    .build();
        }
    }
}
