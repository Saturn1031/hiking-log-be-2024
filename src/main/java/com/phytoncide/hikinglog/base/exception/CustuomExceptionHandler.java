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

    @ExceptionHandler(CursorSizeOutOfRangeException.class)
    protected ResponseEntity<ErrorResponseDTO> CursorSizeOutOfRangeException(final CursorSizeOutOfRangeException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE));
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

    @ExceptionHandler(LikeNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> LikeNotFoundException(final LikeNotFoundException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.LIKE_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.LIKE_NOT_FOUND));
    }

    @ExceptionHandler(BookmarkExistsException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkExistsException(final BookmarkExistsException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_EXISTS.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_EXISTS));
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkNotFoundException(final BookmarkNotFoundException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_NOT_FOUND));
    }

    @ExceptionHandler(BookmarkMountainNameException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkMountainNameException(final BookmarkMountainNameException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_MOUNTAIN_NAME_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_MOUNTAIN_NAME_IS_EMPTY));
    }

    @ExceptionHandler(BookmarkMountainLocationException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkMountainLocationException(final BookmarkMountainLocationException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_MOUNTAIN_LOCATION_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_MOUNTAIN_LOCATION_IS_EMPTY));
    }

    @ExceptionHandler(BookmarkStoreNameException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkStoreNameException(final BookmarkStoreNameException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_STORE_NAME_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_STORE_NAME_IS_EMPTY));
    }

    @ExceptionHandler(BookmarkStoreLocationException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkStoreLocationException(final BookmarkStoreLocationException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_STORE_LOCATION_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_STORE_LOCATION_IS_EMPTY));
    }

    @ExceptionHandler(BookmarkOnlineMallNameException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkOnlineMallNameException(final BookmarkOnlineMallNameException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_ONLINEMALL_NAME_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_ONLINEMALL_NAME_IS_EMPTY));
    }

    @ExceptionHandler(BookmarkOnlineMallLinkException.class)
    protected ResponseEntity<ErrorResponseDTO> BookmarkOnlineMallLinkException(final BookmarkOnlineMallLinkException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.BOOKMARK_ONLINEMALL_LINK_IS_EMPTY.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BOOKMARK_ONLINEMALL_LINK_IS_EMPTY));
    }

    @ExceptionHandler(StoreNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> StoreNotFoundException(final StoreNotFoundException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.STORE_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.STORE_NOT_FOUND));
    }

    @ExceptionHandler(MountainNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> MountainNotFoundException(final MountainNotFoundException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.MOUNTAIN_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.MOUNTAIN_NOT_FOUND));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> NotificationNotFoundException(final NotificationNotFoundException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.NOTIFICATION_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> CommentNotFoundException(final CommentNotFoundException e) {
        log.error("handlePasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.COMMENT_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.COMMENT_NOT_FOUND));
    }
}
