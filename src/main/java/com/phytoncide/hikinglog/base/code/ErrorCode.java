package com.phytoncide.hikinglog.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.CONFLICT, "비밀번호가 일치하지 않습니다."),

    TITLE_IS_EMPTY(HttpStatus.BAD_REQUEST, "제목이 작성되지 않았습니다."),
    CONTENT_IS_EMPTY(HttpStatus.BAD_REQUEST, "내용이 작성되지 않았습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    NOT_PERMITTED_TO_DELETE(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
