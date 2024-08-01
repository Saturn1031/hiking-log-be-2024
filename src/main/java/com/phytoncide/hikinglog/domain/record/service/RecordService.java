package com.phytoncide.hikinglog.domain.record.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.MemberNotFoundException;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.mountain.service.MountainService;
import com.phytoncide.hikinglog.domain.record.dto.RecordRequestDTO;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import com.phytoncide.hikinglog.domain.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecordService {

    private static final Logger log = LoggerFactory.getLogger(RecordService.class);
    private final MemberRepository memberRepository;
    private final RecordRepository recordRepository;
    private final MountainRepository mountainRepository;
    private final MountainService mountainService;

    public String recordHiking(RecordRequestDTO recordRequestDTO, SaveMountainDTO saveMountainDTO) {

        if (mountainRepository.existsByMntilistno(saveMountainDTO.getMntilistno())) {
            MemberEntity member = memberRepository.findByEmail(recordRequestDTO.getEmail());
            MountainEntity mountain = mountainRepository.findByMntilistno(saveMountainDTO.getMntilistno());

            RecordEntity record = RecordEntity.builder()
                    .uid(member)
                    .mid(mountain)
                    .date(recordRequestDTO.getDate())
                    .time(recordRequestDTO.getTime().toMinutes())
                    .build();
            recordRepository.save(record);
            return "등산 기록에 성공했습니다.";
        } else {
            MemberEntity member = memberRepository.findByEmail(recordRequestDTO.getEmail());
            mountainService.saveMountain(saveMountainDTO);

            MountainEntity mountain = mountainRepository.findByMntilistno(saveMountainDTO.getMntilistno());

            RecordEntity record = RecordEntity.builder()
                    .uid(member)
                    .mid(mountain)
                    .date(recordRequestDTO.getDate())
                    .time(recordRequestDTO.getTime().toMinutes())
                    .build();
            recordRepository.save(record);
            return "등산 기록에 성공했습니다.";
        }
    }
}
