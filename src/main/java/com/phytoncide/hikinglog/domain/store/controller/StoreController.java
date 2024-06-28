package com.phytoncide.hikinglog.domain.store.controller;

import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/stay-list")
    public ResponseEntity<ResponseDTO> getStayList(
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude
    ) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(storeService.getAccommodationList(longitude, latitude));


        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_ACCOMMODATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_ACCOMMODATION_LIST, jsonObject));
    }



}
