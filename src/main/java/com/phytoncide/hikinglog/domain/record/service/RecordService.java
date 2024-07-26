package com.phytoncide.hikinglog.domain.record.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.MemberNotFoundException;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.record.dto.RecordRequestDTO;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import com.phytoncide.hikinglog.domain.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final MemberRepository memberRepository;
    private final RecordRepository recordRepository;
    private final MountainRepository mountainRepository;

    public String recordHiking(RecordRequestDTO recordRequestDTO) {

        if(memberRepository.existsByEmail(recordRequestDTO.getEmail())) {
//            if (mountainRepository.existsByMName(recordRequestDTO.))
            MemberEntity member = memberRepository.findByEmail(recordRequestDTO.getEmail());

            RecordEntity record = RecordEntity.builder()
                    .uid(member)
//                    .mid()
                    .date(recordRequestDTO.getDate())
                    .time(recordRequestDTO.getTime().toMinutes())
                    .build();

            recordRepository.save(record);
            return "등산 기록에 성공했습니다.";
        } else {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

    }
}
