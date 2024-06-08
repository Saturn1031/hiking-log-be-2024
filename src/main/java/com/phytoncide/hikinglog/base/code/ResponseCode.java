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
    SUCCESS_GET_PROFILE(HttpStatus.OK, "프로필을 조회했습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
