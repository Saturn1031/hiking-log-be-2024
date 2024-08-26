package com.phytoncide.hikinglog.base.exception;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
}
