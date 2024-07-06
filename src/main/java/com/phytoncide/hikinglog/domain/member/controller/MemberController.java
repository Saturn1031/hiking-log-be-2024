package com.phytoncide.hikinglog.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.awsS3.AmazonS3Service;
import com.phytoncide.hikinglog.domain.member.Service.EmailService;
import com.phytoncide.hikinglog.domain.member.Service.MemberService;
import com.phytoncide.hikinglog.domain.member.config.SecurityConfig;
import com.phytoncide.hikinglog.domain.member.dto.*;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    private final EmailService emailService;
    private final AmazonS3Service amazonS3Service;
    private final MemberService memberService;

    @PostMapping(value ="/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

//    @GetMapping("")
//    public String getCurrentUser() {
//        // 현재 로그인한 사용자의 Authentication 객체를 가져옵니다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // 사용자 정보를 얻습니다.
//        String email = authentication.getName();
//        // 사용자의 권한 등 다른 정보도 가져올 수 있습니다.
//        // List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
//
//        return "Current user: " + email;
//    }

    @GetMapping("/find-email")
    public ResponseEntity<ResponseDTO> findEmail(@RequestParam("phone") String phone) {
        String email = memberService.findEmail(phone);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_FIND_EMAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_FIND_EMAIL, email));
    }

    @PostMapping("/find-password")
    public ResponseEntity<ResponseDTO> findPassword(@RequestParam String email) {
        String password = memberService.findPassword(email);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("[산행록] 비밀번호 재설정")
                .message("재설정된 임시 비밀번호: "+password +"\n"+ "해당 임시 비밀번호로 로그인 후 즉시 비밀번호 변경을 부탁드립니다.")
                .build();
        emailService.sendMail(emailMessage);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_FIND_PASSWORD.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_FIND_PASSWORD, "비밀번호 임시 변경"));

    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String res = memberService.changePassword(authentication.getName(), changePasswordDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CHANGE_PASSWORD.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CHANGE_PASSWORD, res));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ResponseDTO> updateProfile(@RequestParam("profile") String profileDTO, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("이메일" + authentication.getName());

        ObjectMapper mapper = new ObjectMapper();
        ProfileDTO mapperProfileDTO = mapper.readValue(profileDTO, ProfileDTO.class);

        String imageFile = amazonS3Service.saveFile(multipartFile);
        mapperProfileDTO.setImage(imageFile);

        System.out.println("DTO: " + mapperProfileDTO);

        String res = memberService.updateProfile(mapperProfileDTO, authentication.getName());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_PROFILE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_PROFILE, res));
    }

    @GetMapping("profile")
    public ResponseEntity<ResponseDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        GetProfileDTO profile = memberService.getProfile(authentication.getName());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_PROFILE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_PROFILE, profile));
    }
}
