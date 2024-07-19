package com.phytoncide.hikinglog.domain.store.controller;

import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.AccomoListResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantListResponseDTO;
import com.phytoncide.hikinglog.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

        List<AccomoListResponseDTO> res = storeService.getAccommodationList(longitude, latitude);


        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_ACCOMMODATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_ACCOMMODATION_LIST, res));
    }

    @GetMapping("/restaurant-list")
    public ResponseEntity<ResponseDTO> getRestaurantList(
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude
    ) throws IOException, ParseException {

        List<RestaurantListResponseDTO> res = storeService.getRestaurantList(longitude, latitude);


        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_RESTAURANT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_RESTAURANT_LIST, res));
    }

    @GetMapping("/search-stay")
    public ResponseEntity<ResponseDTO> searchStayList(
            @RequestParam("keyword") String keyword
    ) throws IOException, ParseException {

        List<AccomoListResponseDTO> res = storeService.searchAccommodationList(keyword);


        return ResponseEntity
                .status(ResponseCode.SUCCESS_SEARCH_ACCOMMODATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SEARCH_ACCOMMODATION_LIST, res));
    }

    @GetMapping("/search-restaurant")
    public ResponseEntity<ResponseDTO> searchRestaurantList(
            @RequestParam("keyword") String keyword
    ) throws IOException, ParseException {

        List<RestaurantListResponseDTO> res = storeService.searchRestaurantList(keyword);


        return ResponseEntity
                .status(ResponseCode.SUCCESS_SEARCH_RESTAURANT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SEARCH_RESTAURANT_LIST, res));
    }

    @GetMapping("/stay-detail")
    public ResponseEntity<ResponseDTO> getStayDetail(
            @RequestParam("contentId") String contentId
    ) throws IOException, ParseException {
        AccomoDetailResponseDTO res = storeService.getAccommodationDetail(contentId);


        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_ACCOMMODATION_DETAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_ACCOMMODATION_DETAIL, res));
    }

    @GetMapping("/restaurant-detail")
    public ResponseEntity<ResponseDTO> getRestaurantDetail(
            @RequestParam("contentId") String contentId
    ) throws IOException, ParseException {
        RestaurantDetailResponseDTO res = storeService.getRestaurantDetail(contentId);


        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_RESTAURANT_DETAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_RESTAURANT_DETAIL, res));
    }

    @GetMapping("store-list")
    public ResponseEntity<ResponseDTO> getOnlineMallList() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(storeService.getOnlineMallList());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_STORE_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_STORE_LIST, jsonArray));
    }

    @GetMapping("store-detail")
    public ResponseEntity<ResponseDTO> getOnlineMallDetail(
            @RequestParam("id") String id
    ) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(storeService.getOnlineMallDetail(id));

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_STORE_DETAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_STORE_DETAIL, jsonObject));
    }

}
