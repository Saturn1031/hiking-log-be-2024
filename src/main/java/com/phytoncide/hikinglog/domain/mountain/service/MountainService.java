package com.phytoncide.hikinglog.domain.mountain.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.dto.MountainDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MountainService {
    private final MountainRepository mountainRepository;

    public MountainService(MountainRepository mountainRepository) {
        this.mountainRepository = mountainRepository;
    }

    public MountainDTO convertToDTO(){
        MountainDTO mountainDTO = new MountainDTO();
        return mountainDTO;
    }

    public String saveMountain(SaveMountainDTO saveMountainDTO) {
            MountainEntity mountain = MountainEntity.builder()
                    .mntilistno(saveMountainDTO.getMntilistno())
                    .mName(saveMountainDTO.getMName())
                    .location(saveMountainDTO.getLocation())
                    .info(saveMountainDTO.getInfo())
                    .mntiHigh(saveMountainDTO.getMntiHigh())
                    .mImage(saveMountainDTO.getMImage())
                    .build();

            mountainRepository.save(mountain);
            return "산 저장 성공";
    }
}
