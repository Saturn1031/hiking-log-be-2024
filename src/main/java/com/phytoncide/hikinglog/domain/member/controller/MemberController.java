package com.phytoncide.hikinglog.domain.member.controller;

import com.phytoncide.hikinglog.domain.member.Service.MemberService;
import com.phytoncide.hikinglog.domain.member.config.SecurityConfig;
import com.phytoncide.hikinglog.domain.member.dto.JoinDTO;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final MemberService memberService;

    @PostMapping("/join")
    public String join(@RequestBody JoinDTO joinDTO) {
        return memberService.join(joinDTO);
    }

    @GetMapping("")
    public String getCurrentUser() {
        // 현재 로그인한 사용자의 Authentication 객체를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 정보를 얻습니다.
        String email = authentication.getName();
        // 사용자의 권한 등 다른 정보도 가져올 수 있습니다.
        // List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        return "Current user: " + email;
    }
}
