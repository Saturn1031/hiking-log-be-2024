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
        log.error("handleRegisterException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.DUPLICATED_EMAIL.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.DUPLICATED_EMAIL));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleRegisterException(final MemberNotFoundException e) {
        log.error("handleMemberNotFoundException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.MEMBER_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.MEMBER_NOT_FOUND));
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    protected ResponseEntity<ErrorResponseDTO> PasswordNotMatchException(final PasswordNotMatchException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.PASSWORD_NOT_MATCH.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.PASSWORD_NOT_MATCH));
    }

    @ExceptionHandler(BoardsDeleteException.class)
    protected ResponseEntity<ErrorResponseDTO> BoardsDeleteException(final BoardsDeleteException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.NOT_PERMITTED_TO_DELETE.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.NOT_PERMITTED_TO_DELETE));
    }

    @ExceptionHandler(BoardsNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> BoardsNotFoundException(final BoardsNotFoundException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOARD_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOARD_NOT_FOUND));
    }

    @ExceptionHandler(BoardsTitleException.class)
    protected ResponseEntity<ErrorResponseDTO> BoardsTitleException(final BoardsTitleException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.TITLE_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.TITLE_IS_EMPTY));
    }

    @ExceptionHandler(BoardsContentException.class)
    protected ResponseEntity<ErrorResponseDTO> BoardsContentException(final BoardsContentException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.CONTENT_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.CONTENT_IS_EMPTY));
    }
}
