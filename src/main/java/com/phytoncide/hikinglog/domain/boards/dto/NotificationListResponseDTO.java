package com.phytoncide.hikinglog.domain.boards.dto;

import com.phytoncide.hikinglog.domain.boards.entity.NotificationEntity;
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
public class NotificationListResponseDTO {

    private List<NotificationResponseDTO> notificationList;
    private boolean hasNext;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationResponseDTO {
        private String createdAt;
        private Integer id;
        private String title;
        private String content;
        private String url;
        private Integer notifiedUserid;

        public static NotificationResponseDTO toDTO(NotificationEntity entity) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

            return NotificationResponseDTO.builder()
                    .createdAt(entity.getCreatedAt().format(dateTimeFormatter))
                    .id(entity.getNid())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .url(entity.getUrl())
                    .notifiedUserid(entity.getNotifiedUid().getUid())
                    .build();
        }
    }
}
