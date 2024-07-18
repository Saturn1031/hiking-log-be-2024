package com.phytoncide.hikinglog.domain.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.AccomoListResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    @Value("${openApi.stayServiceKey}")
    private String serviceKey;

    @Value("${openApi.stayUrl}")
    private String callBackUrl;

    private static final String FILE_PATH = "src/main/java/com/phytoncide/hikinglog/domain/store/resource/onlineOutdoorMall.json";

    // 숙박 시설 목록 반환
    public List<AccomoListResponseDTO> getAccommodationList(String longitude, String latitude) throws IOException, ParseException {

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

        List<AccomoListResponseDTO> dtoList = new ArrayList<>();

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArray = (JSONArray) items.get("item");

        for (Object obj : itemArray) {
            JSONObject item = (JSONObject) obj;

            AccomoListResponseDTO dto = new AccomoListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg((String) item.get("firstimage"));
            dto.setImg2((String) item.get("firstimage2"));
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        return dtoList;

    }

    // 식당 목록 반환
    public List<RestaurantListResponseDTO> getRestaurantList(String longitude, String latitude) throws IOException, ParseException {

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

        List<RestaurantListResponseDTO> dtoList = new ArrayList<>();

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArray = (JSONArray) items.get("item");

        for (Object obj : itemArray) {
            JSONObject item = (JSONObject) obj;

            RestaurantListResponseDTO dto = new RestaurantListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg((String) item.get("firstimage"));
            dto.setImg2((String) item.get("firstimage2"));
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        return dtoList;

    }

    //숙박 검색
    public List<AccomoListResponseDTO> searchAccommodationList(String keyword) throws IOException, ParseException {

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

        List<AccomoListResponseDTO> dtoList = new ArrayList<>();

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArray = (JSONArray) items.get("item");

        for (Object obj : itemArray) {
            JSONObject item = (JSONObject) obj;

            AccomoListResponseDTO dto = new AccomoListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg((String) item.get("firstimage"));
            dto.setImg2((String) item.get("firstimage2"));
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        return dtoList;

    }

    //식당 검색
    public List<RestaurantListResponseDTO> searchRestaurantList(String keyword) throws IOException, ParseException {

        String urlStr = callBackUrl + "searchKeyword1?" +
                "MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&keyword=" + URLEncoder.encode(keyword, "UTF-8") +
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

        List<RestaurantListResponseDTO> dtoList = new ArrayList<>();

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArray = (JSONArray) items.get("item");

        for (Object obj : itemArray) {
            JSONObject item = (JSONObject) obj;

            RestaurantListResponseDTO dto = new RestaurantListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg((String) item.get("firstimage"));
            dto.setImg2((String) item.get("firstimage2"));
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        return dtoList;

    }

    //숙박 상세
    public AccomoDetailResponseDTO getAccommodationDetail(String contentId) throws IOException, ParseException {

        String urlStr = callBackUrl + "detailCommon1?" +
                "MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&contentId=" + contentId +
                "&contentTypeId=32" + //숙박:32 음식: 39
                "&defaultYN=Y" +
                "&firstImageYN=Y" +
                "&addrinfoYN=Y" +
                "&mapinfoYN=Y" +
                "&overviewYN=Y" +
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

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArray = (JSONArray) items.get("item");

        JSONObject item = (JSONObject) itemArray.get(0);

        AccomoDetailResponseDTO dto = new AccomoDetailResponseDTO();
        dto.setName((String) item.get("title"));
        dto.setContentId((String) item.get("contentid"));
        dto.setAdd((String) item.get("addr1"));
        dto.setImg((String) item.get("firstimage"));
        dto.setImg2((String) item.get("firstimage2"));
        dto.setMapX((String) item.get("mapx"));
        dto.setMapY((String) item.get("mapy"));
        dto.setTel((String) item.get("tel"));
        dto.setIntro((String) item.get("overview"));

        return dto;

    }

    // 식당 상세
    public RestaurantDetailResponseDTO getRestaurantDetail(String contentId) throws IOException, ParseException {

        String urlStr = callBackUrl + "detailCommon1?" +
                "MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&contentId=" + contentId +
                "&contentTypeId=39" + //숙박:32 음식: 39
                "&defaultYN=Y" +
                "&firstImageYN=Y" +
                "&addrinfoYN=Y" +
                "&mapinfoYN=Y" +
                "&overviewYN=Y" +
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

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArray = (JSONArray) items.get("item");

        JSONObject item = (JSONObject) itemArray.get(0);

        RestaurantDetailResponseDTO dto = new RestaurantDetailResponseDTO();
        dto.setName((String) item.get("title"));
        dto.setContentId((String) item.get("contentid"));
        dto.setAdd((String) item.get("addr1"));
        dto.setImg((String) item.get("firstimage"));
        dto.setImg2((String) item.get("firstimage2"));
        dto.setMapX((String) item.get("mapx"));
        dto.setMapY((String) item.get("mapy"));
        dto.setTel((String) item.get("tel"));
        dto.setIntro((String) item.get("overview"));

        return dto;

    }

    //등산 용품 온라인 몰 목록
    public String getOnlineMallList() throws IOException {
        byte[] jsonData = Files.readAllBytes(Paths.get(FILE_PATH));
        String jsonString = new String(jsonData, StandardCharsets.UTF_8);

        return jsonString;
    }

    //등산 용품 온라인 몰 상세
    public String getOnlineMallDetail(String idNum) throws IOException, ParseException {
        byte[] jsonData = Files.readAllBytes(Paths.get(FILE_PATH));
        String jsonString = new String(jsonData, StandardCharsets.UTF_8);

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(jsonString);

        // ID가 1인 객체 찾기
        JSONObject result = null;
        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            String id = (String) jsonObj.get("id"); // "id" 필드가 Long 타입일 경우

            if (id != null && id.equals(idNum)) {
                result = jsonObj;
                break;
            }
        }

        // 결과 반환
        if (result != null) {
            return result.toJSONString(); // ID가 1인 객체의 JSON 문자열 반환
        } else {
            return "ID가 1인 객체를 찾을 수 없습니다.";
        }
    }
}
