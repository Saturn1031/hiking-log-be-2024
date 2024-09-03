package com.phytoncide.hikinglog.domain.boards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateDTO {
    @NotNull
    private String title;

    @NotNull
    private String content;

    private String url;

    @NotNull
    private Integer notifiedUserId;
}
