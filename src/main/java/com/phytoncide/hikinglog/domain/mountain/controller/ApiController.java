package com.phytoncide.hikinglog.domain.mountain.controller;

import com.phytoncide.hikinglog.domain.mountain.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl;

    // 산 정보 목록 검색
    // 이미지 파일명 경로: www.forest.go.kr/images/data/down/mountain/(결과값)
    @GetMapping("/getM/{mountain_Name}")
    public String callApi(
            @PathVariable(value = "mountain_Name") String mountain_Name
    ) throws IOException {
        StringBuilder result = new StringBuilder();
        String mountain_name = URLEncoder.encode(mountain_Name, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + "mntInfoOpenAPI?" +
                "searchWrd=" + mountain_name +
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