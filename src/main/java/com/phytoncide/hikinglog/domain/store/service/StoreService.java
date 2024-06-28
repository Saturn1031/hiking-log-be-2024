package com.phytoncide.hikinglog.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class StoreService {

    @Value("${openApi.stayServiceKey}")
    private String serviceKey;

    @Value("${openApi.stayUrl}")
    private String callBackUrl;

    // 숙박 시설 목록 반환
    public String getAccommodationList(String longitude, String latitude) throws IOException, ParseException {

        String urlStr = callBackUrl + "locationBasedList1?" +
                "MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&mapX=" + longitude +//longitude:경도 127.01612551862054
                "&mapY=" + latitude +//latitude:위도 37.6525631765458
                "&radius=5000" +
                "&contentTypeId=32" + //숙박:32 음식: 39
                "&serviceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        br.close();
        urlConnection.disconnect();

        return jsonObject.toJSONString();

    }

    // 식당 목록 반환
    public String getRestaurantList(String longitude, String latitude) throws IOException, ParseException {

        String urlStr = callBackUrl + "locationBasedList1?" +
                "MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&mapX=" + longitude +//longitude:경도 127.01612551862054
                "&mapY=" + latitude +//latitude:위도 37.6525631765458
                "&radius=5000" +
                "&contentTypeId=39" + //숙박:32 음식: 39
                "&serviceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        br.close();
        urlConnection.disconnect();

        return jsonObject.toJSONString();

    }

    //숙박 검색 (상세)
    public String searchAccommodationList(String keyword) throws IOException, ParseException {

        String urlStr = callBackUrl + "searchKeyword1?" +
                "MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&keyword=" + URLEncoder.encode(keyword, "UTF-8") +
                "&contentTypeId=32" + //숙박:32 음식: 39
                "&serviceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        br.close();
        urlConnection.disconnect();

        return jsonObject.toJSONString();

    }

}
