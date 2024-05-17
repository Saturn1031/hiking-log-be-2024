package com.phytoncide.hikinglog.domain.member.jwt;

public interface JwtProperties {
    String SECRET = "mountain1234";
    int EXPIRATION_TIME = 6000000 * 24 * 7;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
