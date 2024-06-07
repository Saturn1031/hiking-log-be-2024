package com.phytoncide.hikinglog.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage {

    private String to;

    private String subject;

    private String message;
}
