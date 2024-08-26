package com.phytoncide.hikinglog.domain.mountain.controller;

import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.mountain.dto.SearchTrailDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.WeatherDTO;
import com.phytoncide.hikinglog.domain.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MountainController {
    private final MountainService mountainService;

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl;

    @Value("${openApi.top100serviceKey}")
    private String top100serviceKey;

    @Value("${openApi.top100callBackUrl}")
    private String top100callBackUrl;

    // 산 정보 목록 검색
    // 이미지 파일명 경로: www.forest.go.kr/images/data/down/mountain/(결과값)
    @GetMapping("/getM/{mountain_Name}")
    public String callApi(
            @PathVariable(value = "mountain_Name") String mountain_Name
    ) throws IOException {

        return fetchMountainData(mountain_Name);
    }

    // 산 목록 가져오기
    @GetMapping("/getM")
    private String fetchMountainData() throws IOException {

        StringBuilder result = new StringBuilder();
        String urlStr = callBackUrl + "mntInfoOpenAPI2?" +
                "pageNo=1&numOfRows=100" +
                "&ServiceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine).append("\n\r");

        }
        br.close();
        urlConnection.disconnect();

        return result.toString();
    }

    // 100대 명산 목록
    @GetMapping("/getTop100Mountains")
    public String getTop100Mountains() throws IOException {
        StringBuilder result = new StringBuilder();
        String urlStr = top100callBackUrl + top100serviceKey + "&pageNo=1&numOfRows=100";
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine).append("\n\r");

        }
        br.close();
        urlConnection.disconnect();

        return result.toString();
    }

    // 100대 명산 검색
    @GetMapping("/getTop100Mountains/{mountain_Name}")
    public String searchTop100Mountains(@PathVariable(value = "mountain_Name") String mountain_Name) throws IOException {
        StringBuilder result = new StringBuilder();
        String mountain_name = URLEncoder.encode(mountain_Name, StandardCharsets.UTF_8);
        String urlStr = top100callBackUrl + top100serviceKey + "&pageNo=1&numOfRows=100&srchFrtrlNm=" + mountain_name;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine).append("\n\r");

        }
        br.close();
        urlConnection.disconnect();

        return result.toString();
    }

    private String fetchMountainData(String mountain_Name) throws IOException {

        StringBuilder result = new StringBuilder();
        String mountain_name = URLEncoder.encode(mountain_Name, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + "mntInfoOpenAPI2?" +
                "searchWrd=" + mountain_name +
                "&ServiceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine).append("\n\r");

        }
        br.close();
        urlConnection.disconnect();

        return result.toString();
    }


    // 산 정보 목록 검색
    // 이미지 파일명 경로: www.forest.go.kr/images/data/down/mountain/(결과값)
//    @GetMapping("/getM/{mountain_Name}")
//    public String callApi(
//            @PathVariable(value = "mountain_Name") String mountain_Name
//    ) throws IOException {
//        StringBuilder result = new StringBuilder();
//        String mountain_name = URLEncoder.encode(mountain_Name, StandardCharsets.UTF_8);
//        String urlStr = callBackUrl + "mntInfoOpenAPI2?" +
//                "searchWrd=" + mountain_name +
//                "&ServiceKey=" + serviceKey;
//        URL url = new URL(urlStr);
//
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setRequestMethod("GET");
//        urlConnection.setRequestProperty("Content-type", "application/json");
//        urlConnection.setRequestProperty("Accept", "application/json");
//
//        BufferedReader br;
//
//        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
//
//        String returnLine;
//
//        while ((returnLine = br.readLine()) != null) {
//            result.append(returnLine + "\n\r");
//        }
//        br.close();
//        urlConnection.disconnect();
//
//        return result.toString();
//    }


    // 숲길 구간 검색
    @GetMapping("/getR/{mountain_Code}")
    public String callApi2(
            @PathVariable(value = "mountain_Code") String mountain_Code
    ) throws IOException {
        StringBuilder result = new StringBuilder();
        String mountain_code = URLEncoder.encode(mountain_Code, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + "frtrlSectnOpenAPI?" +
                "mntiListNo=" + mountain_code +
                "&ServiceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine + "\n\r");
        }
        br.close();
        urlConnection.disconnect();

        return result.toString();
    }

    // 산 이미지 정보 api
    @GetMapping("/getI/{mountain_Code}")
    public String callApi3(
            @PathVariable(value = "mountain_Code") String mountain_Code
    ) throws IOException {
        StringBuilder result = new StringBuilder();
//        String mountain_code = URLEncoder.encode(mountain_Code, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + "mntInfoImgOpenAPI2?" +
                "mntiListNo=" + mountain_Code +
                "&ServiceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine + "\n\r");
        }
        br.close();
        urlConnection.disconnect();

        return result.toString();
    }

    @GetMapping("/getTrail")
    public ResponseEntity<ResponseDTO> getTrail(
            SearchTrailDTO searchTrailDTO,
            @AuthenticationPrincipal AuthDetails authDetails
    ) throws IOException, ParseException {
        // 산 주소로 법정동 코드 조회
        String emdCd = mountainService.searchEmdCd(searchTrailDTO.getMntiadd().trim());

        // 등산로 조회
        ArrayList<ArrayList<Float>> trail = mountainService.searchTrail(searchTrailDTO.getMntiname(), emdCd);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_SEARCH_TRAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SEARCH_TRAIL, trail));
    }


    @GetMapping("/weather")
    public ResponseEntity<ResponseDTO> getWeather(
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude
    ) throws IOException, ParseException {
//        JSONParser parser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) parser.parse(mountainService.getRealtimeWeather(longitude, latitude));

        WeatherDTO dto = mountainService.getRealtimeWeather(longitude, latitude);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_WEATHER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_WEATHER, dto));

    }
}