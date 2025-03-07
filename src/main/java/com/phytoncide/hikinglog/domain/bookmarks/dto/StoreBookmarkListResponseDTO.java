package com.phytoncide.hikinglog.domain.bookmarks.dto;

import com.phytoncide.hikinglog.domain.bookmarks.entity.BookmarkEntity;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import com.phytoncide.hikinglog.global.enums.BookmarkType;
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
public class StoreBookmarkListResponseDTO {

    private List<StoreBookmarkResponseDTO> bookmarkList;
    private boolean hasNext;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoreBookmarkResponseDTO {
        private String createdAt;
        private String updatedAt;
        private Integer id;
        private Integer storeId;
        private String name;
        private String image;
        private String location;
        private String phone;
        private BookmarkType bookmarkType;

        public static StoreBookmarkResponseDTO toDTO(BookmarkEntity bookmarkEntity,
                                                     StoreEntity storeEntity) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

            return StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO.builder()
                    .createdAt(bookmarkEntity.getCreatedAt().format(dateTimeFormatter))
                    .updatedAt(bookmarkEntity.getUpdatedAt().format(dateTimeFormatter))
                    .id(storeEntity.getSid())
                    .storeId(storeEntity.getContentId())
                    .name(storeEntity.getSName())
                    .image(storeEntity.getSImage())
                    .location(storeEntity.getLocation())
                    .phone(storeEntity.getPhone())
                    .bookmarkType(bookmarkEntity.getBookmarkType())
                    .build();
        }
    }
}
