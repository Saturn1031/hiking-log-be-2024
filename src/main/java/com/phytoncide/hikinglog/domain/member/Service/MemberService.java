package com.phytoncide.hikinglog.domain.member.Service;

import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.dto.JoinDTO;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public String join(JoinDTO joinDTO) {
        if(memberRepository.existsByEmail(joinDTO.getEmail())) {
            return "존재하는 아이디입니다.";

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

}
