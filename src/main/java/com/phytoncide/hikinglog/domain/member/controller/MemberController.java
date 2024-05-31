package com.phytoncide.hikinglog.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.awsS3.AmazonS3Service;
import com.phytoncide.hikinglog.domain.member.Service.MemberService;
import com.phytoncide.hikinglog.domain.member.config.SecurityConfig;
import com.phytoncide.hikinglog.domain.member.dto.JoinDTO;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final AmazonS3Service amazonS3Service;
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDTO> join(@RequestParam("user") String joinDTO, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JoinDTO mapperJoinDTO = mapper.readValue(joinDTO, JoinDTO.class);

        String imageFile = amazonS3Service.saveFile(multipartFile);
        mapperJoinDTO.setImage(imageFile);

        String res = memberService.join(mapperJoinDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_JOIN.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_JOIN, res));
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
