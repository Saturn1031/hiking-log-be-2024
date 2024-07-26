package com.phytoncide.hikinglog.domain.record.controller;

import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.record.dto.RecordRequestDTO;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import com.phytoncide.hikinglog.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/hiking")
    public ResponseEntity<ResponseDTO> recordHiking(@RequestBody RecordRequestDTO recordRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        recordRequestDTO.setEmail(authentication.getName());
        String res = recordService.recordHiking(recordRequestDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RECORD_HIKINGLOG.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RECORD_HIKINGLOG, res));

    }
}
