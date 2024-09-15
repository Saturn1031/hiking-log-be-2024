package com.phytoncide.hikinglog.domain.member.Service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.*;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.dto.ChangePasswordDTO;
import com.phytoncide.hikinglog.domain.member.dto.GetProfileDTO;
import com.phytoncide.hikinglog.domain.member.dto.JoinDTO;
import com.phytoncide.hikinglog.domain.member.dto.ProfileDTO;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    public String join(JoinDTO joinDTO) {

        // 이메일 유효성 검사용 정규식
        String emailRegex = "^[a-zA-Z0-9._%+-]+@(naver\\.com|gmail\\.com|daum\\.net|hanmail\\.net|nate\\.com)$";
        // 핸드폰 번호 유효성 검사용 정규식
        String phoneRegex = "^010\\d{8}$";

        if(memberRepository.existsByEmail(joinDTO.getEmail())) {
            throw new RegisterException(ErrorCode.DUPLICATED_EMAIL);

        } else if (!joinDTO.getEmail().matches(emailRegex)) { // 이메일 형식 검사
            throw new WrongFormatException(ErrorCode.INVALID_EMAIL_FORMAT);

        } else if (!joinDTO.getPhone().matches(phoneRegex)) { // 핸드폰 번호 형식 검사
            throw new WrongPhoneFormatException(ErrorCode.INVALID_PHONE_FORMAT);

        } else {

            MemberEntity member = MemberEntity.builder()
                    .email(joinDTO.getEmail())
                    .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                    .name(joinDTO.getName())
                    .birth(joinDTO.getBirth())
                    .sex(joinDTO.getSex())
                    .phone(joinDTO.getPhone())
                    .image("https://hikinglog-bucket.s3.ap-northeast-2.amazonaws.com/logo.png")
                    .build();
            memberRepository.save(member);
            return "회원가입에 성공했습니다.";
        }
    }

    @Override
    public AuthDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        return new AuthDetails(memberEntity);
    }

    public String findEmail(String phone) {
        Optional<MemberEntity> member;
        if(memberRepository.findByPhone(phone).isPresent()) {
            member = memberRepository.findByPhone(phone);
        } else {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member.get().getEmail();
    }

    public String findPassword(String email) {
        if (memberRepository.existsByEmail(email)) {
            MemberEntity member = memberRepository.findByEmail(email);
            String password = generateRandomPassword();
            member.setPassword(bCryptPasswordEncoder.encode(password));
            memberRepository.save(member);
            return password;
        } else {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public String changePassword(String email, ChangePasswordDTO changePasswordDTO) {
        if (memberRepository.existsByEmail(email)) {
            MemberEntity member = memberRepository.findByEmail(email);
            if(bCryptPasswordEncoder.matches(changePasswordDTO.getOriginPassword(), member.getPassword()))
            {
                member.setPassword(bCryptPasswordEncoder.encode(changePasswordDTO.getNewPassword()));
                memberRepository.save(member);
                return "비밀번호 교체 성공";
            }else {
                throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);
            }

        } else {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public String updateProfile(ProfileDTO profileDTO, String email) {
        if(memberRepository.existsByEmail(email)) {
            MemberEntity member = memberRepository.findByEmail(email);
            member.setName(profileDTO.getName());
            member.setBirth(profileDTO.getBirth());
            member.setSex(profileDTO.getSex());
            member.setPhone(profileDTO.getPhone());
            member.setImage(profileDTO.getImage());
            memberRepository.save(member);
            return "프로필 업데이트 성공";
        } else {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public GetProfileDTO getProfile(String email) {
        if (memberRepository.existsByEmail(email)) {
            MemberEntity member = memberRepository.findByEmail(email);
            GetProfileDTO getProfileDTO = new GetProfileDTO();
            getProfileDTO.setUserid(member.getUid());
            getProfileDTO.setEmail(member.getEmail());
            getProfileDTO.setName(member.getName());
            getProfileDTO.setBirth(member.getBirth());
            getProfileDTO.setSex(member.getSex());
            getProfileDTO.setPhone(member.getPhone());
            getProfileDTO.setImage(member.getImage());
            return getProfileDTO;
        } else {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    private String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public String deleteUser(String email) {
        if (memberRepository.existsByEmail(email)) {
            MemberEntity member = memberRepository.findByEmail(email);

            memberRepository.delete(member);

            return "회원 탈퇴를 성공했습니다.";
        } else {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
