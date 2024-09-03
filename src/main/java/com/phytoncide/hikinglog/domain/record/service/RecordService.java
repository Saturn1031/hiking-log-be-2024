package com.phytoncide.hikinglog.domain.record.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.MemberNotFoundException;
import com.phytoncide.hikinglog.base.exception.RecordNotFoundException;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.mountain.service.MountainService;
import com.phytoncide.hikinglog.domain.record.dto.RecordListResponseDTO;
import com.phytoncide.hikinglog.domain.record.dto.RecordRequestDTO;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import com.phytoncide.hikinglog.domain.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private static final Logger log = LoggerFactory.getLogger(RecordService.class);
    private final MemberRepository memberRepository;
    private final RecordRepository recordRepository;
    private final MountainRepository mountainRepository;
    private final MountainService mountainService;

    public String recordHiking(RecordRequestDTO recordRequestDTO) throws IOException, ParseException {
        System.out.println(recordRequestDTO.getMountainName());

        SaveMountainDTO saveMountainDTO = mountainService.searchMountain(recordRequestDTO.getMountainName());
        System.out.println(saveMountainDTO);

        if (mountainRepository.existsByMntilistno(saveMountainDTO.getMntilistno())) {
            MemberEntity member = memberRepository.findByEmail(recordRequestDTO.getEmail());
            MountainEntity mountain = mountainRepository.findByMntilistno(saveMountainDTO.getMntilistno());
           long num = recordRepository.countByMidAndUid(mountain, member);

            RecordEntity record = RecordEntity.builder()
                    .uid(member)
                    .mid(mountain)
                    .date(recordRequestDTO.getDate())
                    .time(recordRequestDTO.getTime().toMinutes())
                    .number(num+1)
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
                    .number(1)
                    .build();
            recordRepository.save(record);
            return "등산 기록에 성공했습니다.";
        }
    }

    public List<RecordListResponseDTO> getRecordList(String email) {
        MemberEntity member = memberRepository.findByEmail(email);

        List<RecordEntity> list = recordRepository.findByUid(member);

        List<RecordListResponseDTO> dtoList = new ArrayList<>();

        for (RecordEntity item : list) {

            RecordListResponseDTO dto = new RecordListResponseDTO();
            dto.setMName(item.getMid().getMName());
            dto.setNumber(item.getNumber());
            dto.setTime(item.getTime());
            dto.setDate(item.getDate());


            dtoList.add(dto);
        }

        return dtoList;
    }

    public RecordListResponseDTO getRecord(Integer rid) {

        Optional<RecordEntity> record = recordRepository.findByRid(rid);

        RecordListResponseDTO dto = new RecordListResponseDTO();
        dto.setMName(record.get().getMid().getMName());
        dto.setNumber(record.get().getNumber());
        dto.setTime(record.get().getTime());
        dto.setDate(record.get().getDate());

        return dto;
    }

    public String deleteRecord(Integer rid) {
        if (recordRepository.findByRid(rid).isPresent()) {
            Optional<RecordEntity> findRecord = recordRepository.findByRid(rid);
            List<RecordEntity> recordUpdateList = recordRepository.findByMidAndUidAndRidGreaterThan(findRecord.get().getMid(), findRecord.get().getUid(), rid);

            for (RecordEntity record: recordUpdateList) {
                record.setNumber(record.getNumber() - 1);
            }

            recordRepository.saveAll(recordUpdateList);

            recordRepository.deleteById(rid);
        } else {
            throw new RecordNotFoundException(ErrorCode.RECORD_NOT_FOUND);
        }
        return "등산 기록 삭제 성공";
    }


}
