package com.phytoncide.hikinglog.domain.store.service;

import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.AccomoListResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantDetailResponseDTO;
import com.phytoncide.hikinglog.domain.store.dto.RestaurantListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    @Value("${openApi.stayServiceKey}")
    private String serviceKey;

    @Value("${openApi.stayUrl}")
    private String callBackUrl;

    // 숙박 시설 목록 반환
    public List<AccomoListResponseDTO> getAccommodationList(String longitude, String latitude) throws IOException, ParseException {

        String urlStr = callBackUrl + "locationBasedList1?" +
                "numOfRows=10" +
                "&MobileOS=AND" +
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

        WebDriver chromeDriver = null;

        for (Object obj : itemArray) {
            String firstImg;
            JSONObject item = (JSONObject) obj;

            if(!item.get("firstimage").toString().isEmpty()) {
                firstImg = (String) item.get("firstimage");
            } else if (chromeDriver == null) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-extensions");
                chromeDriver = new ChromeDriver(options);

                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            } else {
                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            }

            AccomoListResponseDTO dto = new AccomoListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg(firstImg);
            dto.setImg2("");
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
        }

        return dtoList;

    }

    // 식당 목록 반환
    public List<RestaurantListResponseDTO> getRestaurantList(String longitude, String latitude) throws IOException, ParseException {

        String urlStr = callBackUrl + "locationBasedList1?" +
                "numOfRows=10" +
                "&MobileOS=AND" +
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

        WebDriver chromeDriver = null;

        for (Object obj : itemArray) {
            String firstImg;
            JSONObject item = (JSONObject) obj;

            if(!item.get("firstimage").toString().isEmpty()) {
                firstImg = (String) item.get("firstimage");
            } else if (chromeDriver == null) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-extensions");
                chromeDriver = new ChromeDriver(options);

                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            } else {
                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            }

            RestaurantListResponseDTO dto = new RestaurantListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg(firstImg);
            dto.setImg2("");
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
        }

        return dtoList;

    }

    //숙박 검색
    public List<AccomoListResponseDTO> searchAccommodationList(String keyword) throws IOException, ParseException {

        String urlStr = callBackUrl + "searchKeyword1?" +
                "numOfRows=10" +
                "&MobileOS=AND" +
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

        WebDriver chromeDriver = null;

        for (Object obj : itemArray) {
            String firstImg;
            JSONObject item = (JSONObject) obj;

            if(!item.get("firstimage").toString().isEmpty()) {
                firstImg = (String) item.get("firstimage");
            } else if (chromeDriver == null) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-extensions");
                chromeDriver = new ChromeDriver(options);

                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            } else {
                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            }

            AccomoListResponseDTO dto = new AccomoListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg(firstImg);
            dto.setImg2("");
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
        }

        return dtoList;

    }

    //식당 검색
    public List<RestaurantListResponseDTO> searchRestaurantList(String keyword) throws IOException, ParseException {

        String urlStr = callBackUrl + "searchKeyword1?" +
                "numOfRows=10" +
                "&MobileOS=AND" +
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

        WebDriver chromeDriver = null;

        for (Object obj : itemArray) {
            String firstImg;
            JSONObject item = (JSONObject) obj;

            if(!item.get("firstimage").toString().isEmpty()) {
                firstImg = (String) item.get("firstimage");
            } else if (chromeDriver == null) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-extensions");
                chromeDriver = new ChromeDriver(options);

                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            } else {
                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            }

            RestaurantListResponseDTO dto = new RestaurantListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg(firstImg);
            dto.setImg2("");
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
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

        WebDriver chromeDriver = null;
        String firstImg;

        if(!item.get("firstimage").toString().isEmpty()) {
            firstImg = (String) item.get("firstimage");
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-extensions");
            chromeDriver = new ChromeDriver(options);

            firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
        }

        AccomoDetailResponseDTO dto = new AccomoDetailResponseDTO();
        dto.setName((String) item.get("title"));
        dto.setContentId((String) item.get("contentid"));
        dto.setAdd((String) item.get("addr1"));
        dto.setImg(firstImg);
        dto.setImg2("");
        dto.setMapX((String) item.get("mapx"));
        dto.setMapY((String) item.get("mapy"));
        dto.setTel((String) item.get("tel"));

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
        }

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

        WebDriver chromeDriver = null;
        String firstImg;

        if(!item.get("firstimage").toString().isEmpty()) {
            firstImg = (String) item.get("firstimage");
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-extensions");
            chromeDriver = new ChromeDriver(options);

            firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
        }

        RestaurantDetailResponseDTO dto = new RestaurantDetailResponseDTO();
        dto.setName((String) item.get("title"));
        dto.setContentId((String) item.get("contentid"));
        dto.setAdd((String) item.get("addr1"));
        dto.setImg(firstImg);
        dto.setImg2("");
        dto.setMapX((String) item.get("mapx"));
        dto.setMapY((String) item.get("mapy"));
        dto.setTel((String) item.get("tel"));
        dto.setIntro((String) item.get("overview"));

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
        }

        return dto;

    }

    //등산 용품 온라인 몰 목록
    public String getOnlineMallList() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:static/onlineOutdoorMall.json");
        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    //등산 용품 온라인 몰 상세
    public String getOnlineMallDetail(String idNum) throws IOException, ParseException {
        String jsonString = getOnlineMallList();

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

    // 관광지 목록
    public List<AccomoListResponseDTO> getTourList(String longitude, String latitude) throws IOException, ParseException {

        String urlStr = callBackUrl + "locationBasedList1?" +
                "numOfRows=10" +
                "&MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&mapX=" + longitude +//longitude:경도 127.01612551862054
                "&mapY=" + latitude +//latitude:위도 37.6525631765458
                "&radius=5000" +
                "&contentTypeId=12" + //숙박:32 음식: 39
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

        WebDriver chromeDriver = null;

        for (Object obj : itemArray) {
            String firstImg;
            JSONObject item = (JSONObject) obj;

            if(!item.get("firstimage").toString().isEmpty()) {
                firstImg = (String) item.get("firstimage");
            } else if (chromeDriver == null) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-extensions");
                chromeDriver = new ChromeDriver(options);

                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            } else {
                firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
            }

            AccomoListResponseDTO dto = new AccomoListResponseDTO();
            dto.setName((String) item.get("title"));
            dto.setContentId((String) item.get("contentid"));
            dto.setAdd((String) item.get("addr1"));
            dto.setImg(firstImg);
            dto.setImg2("");
            dto.setMapX((String) item.get("mapx"));
            dto.setMapY((String) item.get("mapy"));
            dto.setTel((String) item.get("tel"));

            dtoList.add(dto);
        }

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
        }

        return dtoList;
    }

    //관광지 상세
    public AccomoDetailResponseDTO getTourDetail(String contentId) throws IOException, ParseException {

        String urlStr = callBackUrl + "detailCommon1?" +
                "MobileOS=AND" +
                "&MobileApp=hikingLog" +
                "&_type=json" +
                "&contentId=" + contentId +
                "&contentTypeId=12" + //숙박:32 음식: 39
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

        WebDriver chromeDriver = null;
        String firstImg;

        if(!item.get("firstimage").toString().isEmpty()) {
            firstImg = (String) item.get("firstimage");
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-extensions");
            chromeDriver = new ChromeDriver(options);

            firstImg = getStoreImage(chromeDriver, (String) item.get("addr1"), (String) item.get("title"));
        }

        AccomoDetailResponseDTO dto = new AccomoDetailResponseDTO();
        dto.setName((String) item.get("title"));
        dto.setContentId((String) item.get("contentid"));
        dto.setAdd((String) item.get("addr1"));
        dto.setImg(firstImg);
        dto.setImg2("");
        dto.setMapX((String) item.get("mapx"));
        dto.setMapY((String) item.get("mapy"));
        dto.setTel((String) item.get("tel"));

        if (chromeDriver != null) {
            chromeDriver.close();
            chromeDriver.quit();
        }

        return dto;

    }

    public String getStoreImage(WebDriver chromeDriver, String addr1, String title) throws IOException {
        String chromeDriverPath = getChromeDriverPath();

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        String img = getImageElement(chromeDriver, addr1, title);

        return img;
    }

    private String getChromeDriverPath() throws IOException {
        // 운영체제와 아키텍처 확인
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource;
        String resourcePath;

        if (os.contains("win")) {
            if (arch.contains("64")) {
                // Windows 64-bit
                resourcePath = "classpath:static/driver/chromedriver-win64/chromedriver.exe";
            } else {
                // Windows 32-bit
                resourcePath = "classpath:static/driver/chromedriver-win32/chromedriver.exe";
            }
        } else if (os.contains("mac")) {
            if (arch.contains("aarch64") || arch.contains("arm64")) {
                // macOS ARM64 (M1/M2)
                resourcePath = "classpath:static/driver/chromedriver-mac-arm64/chromedriver";
            } else {
                // macOS x86_64 (Intel)
                resourcePath = "classpath:static/driver/chromedriver-mac-x64/chromedriver";
            }
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            // Linux 64-bit
            resourcePath = "classpath:static/driver/chromedriver-linux64/chromedriver";
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os + ", architecture: " + arch);
        }

        resource = resourceLoader.getResource(resourcePath);

        return copyResourceToTempFile(resource);
    }

    private String copyResourceToTempFile(Resource resource) throws IOException {
        try (InputStream in = resource.getInputStream()) {
            File tempFile = Files.createTempFile("chromedriver", ".exe").toFile();
            tempFile.deleteOnExit();  // 프로그램 종료 시 임시 파일 삭제
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile.getAbsolutePath();
        }
    }

    private String getImageElement(WebDriver chromeDriver, String addr1, String title) {
        String imgUrl;

        System.out.println("검색어: " + addr1 + " " + title);
        System.out.println("url: " + "https://pcmap.place.naver.com/place/list?query=" + addr1 + " " + title);
        chromeDriver.get("https://pcmap.place.naver.com/place/list?query=" + addr1 + " " + title);

        List<WebElement> imgElements = chromeDriver.findElements(By.tagName("img"));
        if (imgElements.isEmpty()) {
            imgUrl = "";
        } else {
            imgUrl = imgElements.get(0).getAttribute("src");
        }

        return imgUrl;
    }
}
