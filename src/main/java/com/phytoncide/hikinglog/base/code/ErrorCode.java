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

    CURSOR_SIZE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "커서 size 값이 유효하지 않습니다. (유효한 커서 size: 1~2147483647)"),

    TITLE_IS_EMPTY(HttpStatus.BAD_REQUEST, "제목이 작성되지 않았습니다."),
    CONTENT_IS_EMPTY(HttpStatus.BAD_REQUEST, "내용이 작성되지 않았습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    NOT_PERMITTED_TO_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "현재 사용자의 해당 게시글 좋아요를 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알림을 찾을 수 없습니다."),

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
    MOUNTAIN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 산을 찾을 수 없습니다."),
    REGION_INDEX_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 인덱스의 지역을 찾을 수 없습니다."),

    BOOKMARK_EXISTS(HttpStatus.BAD_REQUEST, "이미 동일한 북마크가 존재합니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 북마크를 찾을 수 없습니다."),
    BOOKMARK_MOUNTAIN_NAME_IS_EMPTY(HttpStatus.BAD_REQUEST, "산 이름 속성(name)이 작성되지 않았습니다."),
    BOOKMARK_MOUNTAIN_LOCATION_IS_EMPTY(HttpStatus.BAD_REQUEST, "산 주소 속성(location)이 작성되지 않았습니다."),
    BOOKMARK_STORE_NAME_IS_EMPTY(HttpStatus.BAD_REQUEST, "가게 이름 속성(sName)이 작성되지 않았습니다."),
    BOOKMARK_STORE_LOCATION_IS_EMPTY(HttpStatus.BAD_REQUEST, "가게 주소 속성(location)이 작성되지 않았습니다."),
    BOOKMARK_ONLINEMALL_NAME_IS_EMPTY(HttpStatus.BAD_REQUEST, "등산용품 가게 이름 속성(name)이 작성되지 않았습니다."),
    BOOKMARK_ONLINEMALL_LINK_IS_EMPTY(HttpStatus.BAD_REQUEST, "등산용품 가게 링크 속성(link)이 작성되지 않았습니다."),

    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "등산 기록을 찾을 수 없습니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다.")

    ;

    private final HttpStatus status;
    private final String message;
}
