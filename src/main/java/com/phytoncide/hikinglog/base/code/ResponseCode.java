package com.phytoncide.hikinglog.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS_JOIN(HttpStatus.OK, "회원가입을 성공했습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),
    SUCCESS_FIND_EMAIL(HttpStatus.OK, "이메일을 찾았습니다."),
    SUCCESS_CHANGE_PASSWORD(HttpStatus.OK, "비밀번호 변경을 성공했습니다."),
    SUCCESS_FIND_PASSWORD(HttpStatus.OK, "비밀번호를 찾았습니다."),
    SUCCESS_UPDATE_PROFILE(HttpStatus.OK, "프로필 정보를 업데이트 했습니다."),
    SUCCESS_GET_PROFILE(HttpStatus.OK, "프로필을 조회했습니다."),
    SUCCESS_DELETE_USER(HttpStatus.OK, "회원 정보를 삭제했습니다."),

    SUCCESS_GET_ACCOMMODATION_LIST(HttpStatus.OK, "숙소 목록을 조회했습니다."),
    SUCCESS_GET_ACCOMMODATION_DETAIL(HttpStatus.OK, "숙소 상세를 조회했습니다."),
    SUCCESS_GET_RESTAURANT_LIST(HttpStatus.OK, "식당 목록을 조회했습니다."),
    SUCCESS_GET_RESTAURANT_DETAIL(HttpStatus.OK, "식당 상세를 조회했습니다."),
    SUCCESS_SEARCH_ACCOMMODATION_LIST(HttpStatus.OK, "숙소를 검색했습니다."),
    SUCCESS_SEARCH_RESTAURANT_LIST(HttpStatus.OK, "식당을 검색했습니다."),
    SUCCESS_GET_STORE_LIST(HttpStatus.OK, "등산용품점 온라인몰 목록을 조회했습니다."),
    SUCCESS_GET_STORE_DETAIL(HttpStatus.OK, "등산용품점 온라인몰 상세를 조회했습니다."),
    SUCCESS_GET_TOUR_LIST(HttpStatus.OK, "관광지 목록을 조회했습니다."),
    SUCCESS_GET_TOUR_DETAIL(HttpStatus.OK, "관광지 상세를 조회했습니다."),
  
    SUCCESS_BOARD_WRITE(HttpStatus.OK, "게시글 작성을 성공했습니다."),
    SUCCESS_BOARD_DELETE(HttpStatus.OK, "게시글 삭제를 성공했습니다."),
    SUCCESS_BOARD_UPDATE(HttpStatus.OK, "게시글 수정을 성공했습니다."),
    SUCCESS_BOARD_READ(HttpStatus.OK, "게시글 조회를 성공했습니다."),
    SUCCESS_COMMENT_WRITE(HttpStatus.OK, "댓글 작성을 성공했습니다."),
    SUCCESS_COMMENT_DELETE(HttpStatus.OK, "댓글 삭제를 성공했습니다."),
    SUCCESS_COMMENT_READ(HttpStatus.OK, "댓글 조회를 성공했습니다."),
    SUCCESS_BOARD_LIKE(HttpStatus.OK, "게시글 좋아요 등록을 성공했습니다."),
    SUCCESS_BOARD_UNLIKE(HttpStatus.OK, "게시글 좋아요 삭제를 성공했습니다."),
    SUCCESS_SAVE_NOTIFICATION(HttpStatus.OK, "알림 저장을 성공했습니다."),
    SUCCESS_READ_NOTIFICATION(HttpStatus.OK, "알림 조회를 성공했습니다."),
    SUCCESS_DELETE_NOTIFICATION(HttpStatus.OK, "알림 삭제를 성공했습니다."),

    SUCCESS_RESTAURANT_BOOKMARK_CREATE(HttpStatus.OK, "음식점 북마크 추가를 성공했습니다."),
    SUCCESS_ACCOMMODATION_BOOKMARK_CREATE(HttpStatus.OK, "숙박시설 북마크 추가를 성공했습니다."),
    SUCCESS_ONLINESTORE_BOOKMARK_CREATE(HttpStatus.OK, "등산용품 가게 북마크 추가를 성공했습니다."),
    SUCCESS_MOUNTAIN_BOOKMARK_CREATE(HttpStatus.OK, "산 북마크 추가를 성공했습니다."),
    SUCCESS_STORE_BOOKMARK_DELETE(HttpStatus.OK, "가게 북마크 삭제를 성공했습니다."),
    SUCCESS_ONLINESTORE_BOOKMARK_DELETE(HttpStatus.OK, "등산용품 가게 북마크 삭제를 성공했습니다."),
    SUCCESS_MOUNTAIN_BOOKMARK_DELETE(HttpStatus.OK, "산 북마크 삭제를 성공했습니다."),
    SUCCESS_BOOKMARK_READ_ALL(HttpStatus.OK, "전체 북마크 조회를 성공했습니다."),
    SUCCESS_BOOKMARK_READ_MOUNTAIN(HttpStatus.OK, "산 북마크 조회를 성공했습니다."),
    SUCCESS_BOOKMARK_READ_RESTAURANT(HttpStatus.OK, "음식점 북마크 조회를 성공했습니다."),
    SUCCESS_BOOKMARK_READ_ACCOMMODATION(HttpStatus.OK, "숙박시설 북마크 조회를 성공했습니다."),
    SUCCESS_BOOKMARK_READ_ONLINESTORE(HttpStatus.OK, "등산용품 가게 북마크 조회를 성공했습니다."),
    SUCCESS_GET_WEATHER(HttpStatus.OK, "날씨 정보 조회를 성공했습니다."),

    SUCCESS_RECORD_HIKINGLOG(HttpStatus.OK, "등산 정보 기록에 성공했습니다."),
    SUCCESS_GET_RECORD_LIST(HttpStatus.OK, "등산 정보 기록 목록 조회를 성공했습니다."),
    SUCCESS_SEARCH_TRAIL(HttpStatus.OK, "등산로 조회를 성공했습니다."),
    SUCCESS_DELETE_RECORD(HttpStatus.OK, "등산 기록 삭제를 성공했습니다."),
    SUCCESS_GET_RECORD_DETAIL(HttpStatus.OK, "등산 기록 상세 조회를 성공했습니다."),

    SUCCESS_GET_MOUNTAIN_DETAIL(HttpStatus.OK, "산 상세 조회를 성공했습니다."),
    SUCCESS_GET_TOP100MOUNTAIN_BY_REGION_LIST(HttpStatus.OK, "100대 명산 지역별 목록 조회를 성공했습니다."),
    SUCCESS_GET_ALL_MOUNTAIN_BY_REGION_LIST(HttpStatus.OK, "전국 산 지역별 목록 조회를 성공했습니다."),


    ;

    private final HttpStatus status;
    private final String message;
}
