package com.phytoncide.hikinglog.domain.mountain.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MountainController {

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl;

    // 백대 명산 리스트
    private final List<String> top100Mountains = Arrays.asList(
            "관악산", "도봉산", "북한산", "수락산", "불암산", "청계산", "금정산", "비슬산", "마니산", "고려산", "무등산", "계룡산", "신불산",
            "가지산", "연인산", "명지산", "화악산", "유명산", "운악산", "남한산", "축령산", "서리산", "천마산", "소요산", "광교산", "수리산",
            "용문산", "백운산", "감악산", "명성산", "검단산", "감악산", "노인봉", "대암산", "두타산", "덕항산", "응봉산", "점봉산", "백덕산",
            "태화산", "마대산", "백운산", "치악산", "방태산", "설악산", "가리왕산", "민둥산", "오봉산", "삼악산", "가리산", "태백산", "함백산",
            "금대봉", "오대산", "선자령", "공작산", "팔봉산", "계방산", "문암산", "용화산", "청화산", "칠보산", "도락산", "민주지산", "천태산",
            "금수산", "월악산", "서대산", "진악산", "대둔산", "가야산", "덕숭산", "광덕산", "칠갑산", "오서산", "용봉산", "선운산", "모악산",
            "지리산", "바래봉", "반야봉", "만행산", "적상산", "덕유산", "변산", "강천산", "백암산", "장안산", "신무산", "백운산", "내장산",
            "구봉산", "마이산", "운장산", "천상데미", "덕룡산", "팔영산", "동악산", "백운산", "추월산", "병풍산", "조계산", "불갑산", "월출산",
            "방장산", "축령산", "제암산", "천관산", "두륜산", "달마산", "깃대봉", "영취산", "남산", "토함산", "금오산", "황악산", "희양산",
            "주흘산", "조령산", "대야산", "황장산", "청량산", "문수산", "일월산", "속리산", "구병산", "내연산", "소백산", "성인봉", "운문산",
            "주왕산", "팔공산", "금산", "무학산", "천주산", "재약산", "천황산", "천성산", "연화산", "화왕산", "지리산", "미륵산", "황석산",
            "남덕유산", "황매산", "가야산", "남산제일봉", "계룡산", "한라산"
    );

    @GetMapping("/getM/{mountain_Name}")
    public String callApi(
            @PathVariable(value = "mountain_Name") String mountain_Name
    ) throws IOException {
        return fetchMountainData(mountain_Name);
    }

    @GetMapping("/getTop100Mountains")
    public List<String> getTop100Mountains() {
        List<String> mountainDataList = new ArrayList<>();
        for (String mountainName : top100Mountains) {
            try {
                mountainDataList.add(fetchMountainData(mountainName));
            } catch (IOException e) {
                e.printStackTrace();
                mountainDataList.add("Error fetching data for " + mountainName);
            }
        }
        return mountainDataList;
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
        String mountain_code = URLEncoder.encode(mountain_Code, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + "mntInfoImgOpenAPI?" +
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
}