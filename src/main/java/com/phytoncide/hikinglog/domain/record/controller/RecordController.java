package com.phytoncide.hikinglog.domain.record.controller;

import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.record.dto.RecordListResponseDTO;
import com.phytoncide.hikinglog.domain.record.dto.RecordRequestDTO;
import com.phytoncide.hikinglog.domain.record.dto.RecordRequestWrapper;
import com.phytoncide.hikinglog.domain.record.entity.RecordEntity;
import com.phytoncide.hikinglog.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/hiking")
    public ResponseEntity<ResponseDTO> recordHiking(@RequestBody RecordRequestDTO recordRequestDTO) throws IOException, ParseException {
//        RecordRequestDTO recordRequestDTO = recordRequestWrapper.getRecordRequestDTO();
//        SaveMountainDTO saveMountainDTO = recordRequestWrapper.getSaveMountainDTO();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        recordRequestDTO.setEmail(authentication.getName());

        System.out.println(recordRequestDTO.getMountainName());
        System.out.println(recordRequestDTO.getDate());

        String res = recordService.recordHiking(recordRequestDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RECORD_HIKINGLOG.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RECORD_HIKINGLOG, res));

    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> getRecordList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<RecordListResponseDTO> list = recordService.getRecordList(authentication.getName());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_RECORD_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_RECORD_LIST, list));
    }

    @GetMapping("/detail")
    public ResponseEntity<ResponseDTO> getRecord(@RequestParam("id")Integer rid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        RecordListResponseDTO dto = recordService.getRecord(rid);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_RECORD_DETAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_RECORD_DETAIL, dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteRecord(@RequestParam("id")Integer rid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String res = recordService.deleteRecord(rid);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_RECORD.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_RECORD, res));
    }
}
