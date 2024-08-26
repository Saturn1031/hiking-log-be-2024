package com.phytoncide.hikinglog.domain.boards.dto;

import com.phytoncide.hikinglog.domain.boards.entity.CommentEntity;
import com.phytoncide.hikinglog.domain.boards.entity.NotificationEntity;
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
public class NotificationListResponseDTO {

    private List<NotificationResponseDTO> notificationList;
    private boolean hasNext;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationResponseDTO {
        private LocalDateTime createdAt;
        private Integer id;
        private String title;
        private String content;
        private String url;
        private Integer notifiedUserid;

        public static NotificationResponseDTO toDTO(NotificationEntity entity) {
            return NotificationResponseDTO.builder()
                    .createdAt(entity.getCreatedAt())
                    .id(entity.getNid())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .url(entity.getUrl())
                    .notifiedUserid(entity.getNotifiedUid().getUid())
                    .build();
        }
    }
}
