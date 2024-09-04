package com.phytoncide.hikinglog.domain.mountain.controller;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.base.exception.RegionIndexNotFoundException;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.mountain.dto.DetailMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.SearchTrailDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.WeatherDTO;
import com.phytoncide.hikinglog.domain.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


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

    // 100대 명산 지역 필터링
    @GetMapping("/getTop100MountainsByRegion/{region_array_index}")
    public ResponseEntity<ResponseDTO> getTop100MountainsByRegion(
            @PathVariable("region_array_index") Integer region_array_index
    ) throws IOException, ParseException {
        JSONArray res;

        switch (region_array_index) {
            case 0:
                res = mountainService.getTop100MountainsByRegion("");
                break;
            case 1:
                res = mountainService.getTop100MountainsByRegion("서울특별시");
                break;
            case 2:
                res = mountainService.getTop100MountainsByRegion("경기도");
                break;
            case 3:
                res = mountainService.getTop100MountainsByRegion("강원특별자치도");
                break;
            case 4:
                res = mountainService.getTop100MountainsByRegion("충청북도");
                break;
            case 5:
                res = mountainService.getTop100MountainsByRegion("충청남도");
                break;
            case 6:
                res = mountainService.getTop100MountainsByRegion("전라북도");
                break;
            case 7:
                res = mountainService.getTop100MountainsByRegion("전라남도");
                break;
            case 8:
                res = mountainService.getTop100MountainsByRegion("경상북도");
                break;
            case 9:
                res = mountainService.getTop100MountainsByRegion("경상남도");
                break;
            case 10:
                res = mountainService.getTop100MountainsByRegion("제주특별자치도");
                break;
            case 11:
                res = mountainService.getTop100MountainsByRegion("부산광역시");
                break;
            case 12:
                res = mountainService.getTop100MountainsByRegion("대구광역시");
                break;
            case 13:
                res = mountainService.getTop100MountainsByRegion("인천광역시");
                break;
            case 14:
                res = mountainService.getTop100MountainsByRegion("광주광역시");
                break;
            case 15:
                res = mountainService.getTop100MountainsByRegion("대전광역시");
                break;
            case 16:
                res = mountainService.getTop100MountainsByRegion("울산광역시");
                break;
            default:
                throw new RegionIndexNotFoundException(ErrorCode.REGION_INDEX_NOT_FOUND);
        }

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_TOP100MOUNTAIN_BY_REGION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_TOP100MOUNTAIN_BY_REGION_LIST, res));
    }

    // 100대 명산 검색
    @GetMapping("/getTop100Mountains/{mountain_Name}")
    public ResponseEntity<SaveMountainDTO> getMountainList(@PathVariable("mountain_Name") String mountain_Name) throws IOException {
        SaveMountainDTO saveMountainList = mountainService.getMountains(mountain_Name);
        return ResponseEntity.ok(saveMountainList);
    }
//    public String searchTop100Mountains(@PathVariable(value = "mountain_Name") String mountain_Name) throws IOException {
//        StringBuilder result = new StringBuilder();
//        String mountain_name = URLEncoder.encode(mountain_Name, StandardCharsets.UTF_8);
//        String urlStr = top100callBackUrl + top100serviceKey + "&pageNo=1&numOfRows=100&srchFrtrlNm=" + mountain_name;
//        URL url = new URL(urlStr);
//
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setRequestMethod("GET");
//        urlConnection.setRequestProperty("Content-type", "application/json");
//        urlConnection.setRequestProperty("Accept", "application/json");
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
//        String returnLine;
//
//        while ((returnLine = br.readLine()) != null) {
//            result.append(returnLine).append("\n\r");
//
//        }
//        br.close();
//        urlConnection.disconnect();
//
//        return result.toString();
//    }

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
            @RequestParam("address") String address
    ) throws IOException, ParseException {

        WeatherDTO dto = mountainService.getRealtimeWeather(address);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_WEATHER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_WEATHER, dto));

    }

//    @GetMapping("/dust")
//    public ResponseEntity<ResponseDTO> getWeather(@RequestParam("sido") String sido) throws IOException, ParseException {
//
//        JSONObject dto = mountainService.getRealTimeDust(sido);
//        return ResponseEntity
//                .status(ResponseCode.SUCCESS_GET_WEATHER.getStatus().value())
//                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_WEATHER, dto));
//
//    }

    @GetMapping("/mountain/detail")
    public ResponseEntity<ResponseDTO> getMountainDetail(
            @RequestParam("name") String mountainName,
            @RequestParam("number") String mountainNumber
    ) throws IOException, ParseException {

        DetailMountainDTO dto = mountainService.searchMountain(mountainName, mountainNumber);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_MOUNTAIN_DETAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_MOUNTAIN_DETAIL, dto));

    }
}