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

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkListResponseDTO {

    private List<BookmarkResponseDTO> bookmarkList;
    private boolean hasNext;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookmarkResponseDTO {
        private String createdAt;
        private String updatedAt;
        private Integer id;
        private Integer storeId;
        private Integer mntilistno;
        private String name;
        private String image;
        private String location;
        private String phone;
        private String link;
        private Double mntiHigh;
        private String mntiInfo;
        private BookmarkType bookmarkType;

        public static BookmarkResponseDTO storeToDTO(BookmarkEntity bookmarkEntity,
                                                     StoreEntity storeEntity) {
            return BookmarkResponseDTO.builder()
                    .createdAt(bookmarkEntity.getCreatedAt().toString())
                    .updatedAt(bookmarkEntity.getUpdatedAt().toString())
                    .id(storeEntity.getSid())
                    .storeId(storeEntity.getContentId())
                    .name(storeEntity.getSName())
                    .image(storeEntity.getSImage())
                    .location(storeEntity.getLocation())
                    .phone(storeEntity.getPhone())
                    .bookmarkType(bookmarkEntity.getBookmarkType())
                    .build();
        }

        public static BookmarkResponseDTO onlineOutdoorMallToDTO(BookmarkEntity bookmarkEntity,
                                                                 OnlineOutdoorMallEntity onlineOutdoorMallEntity) {
            return BookmarkResponseDTO.builder()
                    .createdAt(bookmarkEntity.getCreatedAt().toString())
                    .updatedAt(bookmarkEntity.getUpdatedAt().toString())
                    .id(onlineOutdoorMallEntity.getOid())
                    .storeId(onlineOutdoorMallEntity.getStoreId())
                    .name(onlineOutdoorMallEntity.getOName())
                    .image(onlineOutdoorMallEntity.getOImage())
                    .link(onlineOutdoorMallEntity.getLink())
                    .bookmarkType(bookmarkEntity.getBookmarkType())
                    .build();
        }

        public static BookmarkResponseDTO mountainToDTO(BookmarkEntity bookmarkEntity,
                                                        MountainEntity mountainEntity) {
            return BookmarkResponseDTO.builder()
                    .createdAt(bookmarkEntity.getCreatedAt().toString())
                    .updatedAt(bookmarkEntity.getUpdatedAt().toString())
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
