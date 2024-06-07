package com.phytoncide.hikinglog.domain.member.Service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.MemberNotFoundException;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.dto.ChangePasswordDTO;
import com.phytoncide.hikinglog.domain.member.dto.JoinDTO;
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
        if(memberRepository.existsByEmail(joinDTO.getEmail())) {
            throw new RegisterException(ErrorCode.DUPLICATED_EMAIL);

        } else {
            MemberEntity member = MemberEntity.builder()
                    .email(joinDTO.getEmail())
                    .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                    .name(joinDTO.getName())
                    .birth(joinDTO.getBirth())
                    .sex(joinDTO.getSex())
                    .phone(joinDTO.getPhone())
                    .image(joinDTO.getImage())
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

    public String changePassword(ChangePasswordDTO changePasswordDTO) {
        if (memberRepository.existsByEmail(changePasswordDTO.getEmail())) {
            MemberEntity member = memberRepository.findByEmail(changePasswordDTO.getEmail());
            member.setPassword(bCryptPasswordEncoder.encode(changePasswordDTO.getPassword()));
            memberRepository.save(member);
            return "비밀번호 교체 성공";
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
}
