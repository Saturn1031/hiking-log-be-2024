package com.phytoncide.hikinglog.base.exception;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustuomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustuomExceptionHandler.class);

    @ExceptionHandler(RegisterException.class)
    protected ResponseEntity<ErrorResponseDTO> handleRegisterException(final RegisterException e) {
        log.error("haddleRegisterException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.DUPLICATED_EMAIL.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.DUPLICATED_EMAIL));
    }
}
