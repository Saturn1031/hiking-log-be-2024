package com.phytoncide.hikinglog.domain.mountain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.DataNotFoundException;
import com.phytoncide.hikinglog.base.exception.RecordNotFoundException;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.dto.*;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import lombok.RequiredArgsConstructor;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.client.RestTemplate;

import static io.netty.resolver.HostsFileEntriesProvider.parser;

@Service
public class MountainService {
    private final MountainRepository mountainRepository;
    private final WeatherService weatherService;
    private RestTemplate restTemplate;

    @Value("${openApi.trailServiceKey}")
    private String trailServiceKey;

    @Value("${openApi.trailUrl}")
    private String trailUrl;

    @Value("${openApi.weatherServiceKey}")
    private String weatherServiceKey;

    @Value("${openApi.weatherUrl}")
    private String weatherUrl;

    @Value("${openApi.dustServiceKey}")
    private String dustServiceKey;

    @Value("${openApi.dustUrl}")
    private String dustUrl;

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl;

    @Value("${openApi.top100serviceKey}")
    private String top100serviceKey;

    @Value("${openApi.top100callBackUrl}")
    private String top100callBackUrl;

    @Value("${openApi.observeServiceKey}")
    private String observeServiceKey;

    @Value("${openApi.observeUrl}")
    private String observeUrl;

    @Value("${openApi.kakaoAPIKey}")
    private String kakaoAPIKey;

    @Autowired
    public MountainService(MountainRepository mountainRepository, WeatherService weatherService) {
        this.mountainRepository = mountainRepository;
        this.weatherService = weatherService;
        this.restTemplate = restTemplate;
    }

    public SaveMountainDTO getMountains(String mName) throws IOException {
        String mountainName = URLEncoder.encode(mName, StandardCharsets.UTF_8);

        // URL1: 산의 고유 ID 가져오기 (JSON)
        StringBuilder result1 = new StringBuilder();
        String urlStr1 = callBackUrl + "mntInfoOpenAPI2?" +
                "searchWrd=" + mountainName +
                "&ServiceKey=" + serviceKey;
        URL url1 = new URL(urlStr1);

        HttpURLConnection urlConnection1 = (HttpURLConnection) url1.openConnection();
        urlConnection1.setRequestMethod("GET");
        urlConnection1.setRequestProperty("Content-type", "application/json");
        urlConnection1.setRequestProperty("Accept", "application/json");

        BufferedReader br1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream(), StandardCharsets.UTF_8));
        String returnLine1;

        while ((returnLine1 = br1.readLine()) != null) {
            result1.append(returnLine1).append("\n\r");
        }
        br1.close();
        urlConnection1.disconnect();
        System.out.println("Converted JSON: " + result1);

        // JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode1 = mapper.readTree(result1.toString());
        JsonNode itemsNode = rootNode1.path("response").path("body").path("items").path("item");

        // 필요한 값 추출
        Integer mntilistno = itemsNode.path("mntilistno").asInt();
        System.out.println("mntilistno: " + mntilistno);

        // URL2: 나머지 정보 가져오기 (XML)
        StringBuilder result2 = new StringBuilder();
        String urlStr2 = top100callBackUrl + top100serviceKey + "&pageNo=1&numOfRows=100&srchFrtrlNm=" + mountainName;
//        ResponseEntity<String> response2 = restTemplate.getForEntity(urlStr2, String.class);
        URL url2 = new URL(urlStr2);

        HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
        urlConnection2.setRequestMethod("GET");
        urlConnection2.setRequestProperty("Content-type", "application/json");
        urlConnection2.setRequestProperty("Accept", "application/json");

        BufferedReader br2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream(), StandardCharsets.UTF_8));
        String returnLine2;

        while ((returnLine2 = br2.readLine()) != null) {
            result2.append(returnLine2).append("\n\r");

        }
        br2.close();
        urlConnection2.disconnect();

        // XML을 JSON으로 변환
        String xmlResponse = result2.toString();
        org.json.JSONObject jsonObject2 = XML.toJSONObject(xmlResponse);

        // JSON 문자열로 변환
        String jsonString2 = jsonObject2.toString();
        System.out.println("Converted JSON: " + jsonString2);

        // JSON 파서로 JSON 객체로 변환
        JsonNode rootNode2 = mapper.readTree(jsonString2);
        JsonNode rows = rootNode2.path("response").path("body").path("items").path("item");

        // DTO 리스트 생성
        SaveMountainDTO saveMountainDTO = SaveMountainDTO.builder()
                .mntilistno(rows.path("mtnCd").asInt())
                .mName(rows.path("frtrlNm").asText())  // 산 이름
                .location(rows.path("addrNm").asText()) // 주소
                .info(rows.path("ctpvNm").asText() + " (" + rows.path("mtnCd").asText() + ")") // 산 코드 정보
                .mntiHigh(rows.path("aslAltide").asDouble()) // 고도
                .mImage(null) // 이미지 URL이 있을 경우 추가
                .build();

        return saveMountainDTO;
    }

    public JSONArray getTop100MountainsByRegion(String regionName) throws IOException, ParseException {
        String urlStr = top100callBackUrl + top100serviceKey + "&pageNo=1&numOfRows=100&type=json&srchCtpvNm=" + URLEncoder.encode(regionName, StandardCharsets.UTF_8);
        URL url = new URL(urlStr);

        System.out.println("url: " + urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        br.close();
        urlConnection.disconnect();

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");

        return item;
    }


    public MountainDTO convertToDTO(){
        MountainDTO mountainDTO = new MountainDTO();
        return mountainDTO;
    }

    public String saveMountain(SaveMountainDTO saveMountainDTO) {
        MountainEntity mountain = MountainEntity.builder()
                .mntilistno(saveMountainDTO.getMntilistno())
                .mName(saveMountainDTO.getMName())
                .location(saveMountainDTO.getLocation())
                .info(saveMountainDTO.getInfo())
                .mntiHigh(saveMountainDTO.getMntiHigh())
                .mImage(saveMountainDTO.getMImage())
                .build();

        mountainRepository.save(mountain);
        return "산 저장 성공";
    }

    public String searchEmdCd(String mntiadd) throws IOException, ParseException {
        String emdCd = "";

        String urlStr = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=*00000000";
        URL url = new URL(urlStr);

        System.out.println(url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        br.close();
        urlConnection.disconnect();

        JSONArray regcodes = (JSONArray) jsonObject.get("regcodes");

        String[] add = mntiadd.split(" ");
        String firstAdd = add[0];
        String secondAdd = add[0] + " " + add[1];
        String thirdAdd = mntiadd;
        for (int i = 0; i < regcodes.size(); i++) {
            JSONObject codeAndName = (JSONObject) regcodes.get(i);
            String addName = (String) codeAndName.get("name");
            if (firstAdd.equals(addName)) {
                String addCode = (String) codeAndName.get("code");
                String firstAddCode = addCode.substring(0, 2);

                String urlStr2 = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=" + firstAddCode + "*00000";
                URL url2 = new URL(urlStr2);

                System.out.println(url2);

                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                urlConnection2.setRequestMethod("GET");
                urlConnection2.setRequestProperty("Content-type", "application/json");
                urlConnection2.setRequestProperty("Accept", "application/json");

                BufferedReader br2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream(), StandardCharsets.UTF_8));

                JSONParser parser2 = new JSONParser();
                JSONObject jsonObject2 = (JSONObject) parser2.parse(br2);

                br2.close();
                urlConnection2.disconnect();

                JSONArray regcodes2 = (JSONArray) jsonObject2.get("regcodes");

                for (int j = 0; j < regcodes2.size(); j++) {
                    JSONObject codeAndName2 = (JSONObject) regcodes2.get(j);
                    String addName2 = (String) codeAndName2.get("name");

                    if (secondAdd.equals(addName2)) {
                        String addCode2 = (String) codeAndName2.get("code");
                        String secondAddCode = addCode2.substring(0, 5);

                        String urlStr3 = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=" + secondAddCode + "*";
                        URL url3 = new URL(urlStr3);

                        System.out.println(url3);

                        HttpURLConnection urlConnection3 = (HttpURLConnection) url3.openConnection();
                        urlConnection3.setRequestMethod("GET");
                        urlConnection3.setRequestProperty("Content-type", "application/json");
                        urlConnection3.setRequestProperty("Accept", "application/json");

                        BufferedReader br3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream(), StandardCharsets.UTF_8));

                        JSONParser parser3 = new JSONParser();
                        JSONObject jsonObject3 = (JSONObject) parser3.parse(br3);

                        br3.close();
                        urlConnection3.disconnect();

                        JSONArray regcodes3 = (JSONArray) jsonObject3.get("regcodes");

                        for (int k = 0; k < regcodes3.size(); k++) {
                            JSONObject codeAndName3 = (JSONObject) regcodes3.get(k);
                            String addName3 = (String) codeAndName3.get("name");

                            if (thirdAdd.equals(addName3)) {
                                String addCode3 = (String) codeAndName3.get("code");
                                emdCd = addCode3.substring(0, 8);
                            }
                        }
                    }
                }
            }
        }

        return emdCd;
    }

    public ArrayList<ArrayList<Float>> searchTrail(String mntiname, String emdCd) throws IOException, ParseException {
        ArrayList<ArrayList<Float>> trail = new ArrayList<ArrayList<Float>>();

        String mntinameKor = URLEncoder.encode(mntiname, StandardCharsets.UTF_8);
        String urlStr = trailUrl + "?"
                + "request=GetFeature" + "&data=LT_L_FRSTCLIMB" + "&crs=EPSG:4326" + "&format=json&errorformat=json"
                + "&key=" + trailServiceKey
                + "&size=1000&page=1"
                + "&attrfilter=mntn_nm:=:" + mntinameKor + "|emdCd:=:" + emdCd;
        URL url = new URL(urlStr);

        System.out.println(url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        br.close();
        urlConnection.disconnect();

        JSONObject response = (JSONObject) jsonObject.get("response");

        if (!response.containsKey("result")) {
            return trail;
        }

        JSONObject result = (JSONObject) response.get("result");
        JSONObject featureCollection = (JSONObject) result.get("featureCollection");
        JSONArray features = (JSONArray) featureCollection.get("features");



        for (int i = 0; i < features.size(); i++) {
            JSONObject feature = (JSONObject) features.get(i);
            JSONObject geometry = (JSONObject) feature.get("geometry");
            JSONArray coordinates = (JSONArray) geometry.get("coordinates");

            for (int j = 0; j < coordinates.size(); j++) {
                JSONArray coordinate = (JSONArray) coordinates.get(j);

                for (int k = 0; k < coordinate.size(); k++) {
                    JSONArray point = (JSONArray) coordinate.get(k);
                    trail.add(point);
                }
            }
        }

        return trail;
    }

    // 날씨 정보
    public WeatherDTO getRealtimeWeather(String address) throws IOException, ParseException {

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(dateFormatter);

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        String formattedTime = currentTime.format(timeFormatter);

        if(Integer.parseInt(formattedTime) < 611) {
            Integer nowDate = Integer.parseInt(formattedDate) - 1;
            formattedDate = String.valueOf(nowDate - 1);
        }

        // 주소 위도 경도 변환
        AddressXYDTO xydto = changeAddressTOXY(address);

        // 위도 경도를 행정구역 x,y 값으로 변환
        WeatherXYDTO weatherXYDTO = weatherService.getWeatherXY(xydto.getLongitude(), xydto.getLatitude());

        String urlStr = weatherUrl + "/getUltraSrtNcst?" +
                "&serviceKey=" + weatherServiceKey +
                "&numOfRows=20" +
                "&pageNo=1" +
                "&dataType=JSON" +
                "&base_date="+ formattedDate +
                "&base_time=0600" +
                "&nx="+ weatherXYDTO.getX() +
                "&ny="+ weatherXYDTO.getY();
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
        JSONObject temperature = (JSONObject) itemArray.get(3); //T1H
        JSONObject rain = (JSONObject) itemArray.get(0); //PTY
        JSONObject wind = (JSONObject) itemArray.get(7); //WSD

        System.out.println(itemArray);


        WeatherDTO dto = new WeatherDTO();
        dto.setTemperature((String) temperature.get("obsrValue"));
        dto.setRain(returnRain((String)rain.get("obsrValue")));
        dto.setWind(returnWind((String)wind.get("obsrValue")));

        return dto;

    }

    public String returnRain(String rainValue) {
        return switch (rainValue) {
            case "0" -> "맑음";
            case "1" -> "비";
            case "2" -> "비와 눈";
            case "3" -> "눈";
            case "4" -> "소나기";
            case "5" -> "빗방울";
            case "6" -> "빗방울과 눈날림";
            case "7" -> "눈날림";
            default -> "알 수 없음";
        };
    }

    public String returnWind(String windValue) {
        return switch (windValue) {
            case "0","1","2","3" -> "바람이 약합니다";
            case "4","5","6","7","8" -> "바람이 약간 강합니다";
            case "9","10","11","12","13" -> "바람이 강합니다";
            default -> "바람이 매우 강합니다";
        };
    }

    public JSONObject getRealTimeDust(String sido) throws IOException, ParseException {

        String urlStr = dustUrl + "/getCtprvnRltmMesureDnsty?" +
                "sidoName=부산"+
                "&pageNo=1" +
                "&numOfRows=10" +
                "&returnType=json" +
                "&serviceKey=" + dustServiceKey +
                "&ver=1.2";
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

        return jsonObject;

    }

    // 산 상세
    public DetailMountainDTO searchMountain(String mountainName, String mountainNum) throws IOException, ParseException {

        StringBuilder result = new StringBuilder();
        String mountain_name = URLEncoder.encode(mountainName, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + "mntInfoOpenAPI2?" +
                "searchWrd=" + mountain_name +
                "&ServiceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        System.out.println(jsonObject);

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");

        if (body.get("items").equals("")) { // 데이터가 없는 경우
            throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND);
        }

        JSONObject items = (JSONObject) body.get("items");
        JSONObject item = null;

        Object itemObject = items.get("item");
        JSONArray itemArray;

        if (itemObject instanceof JSONArray) {
            itemArray = (JSONArray) itemObject;
        } else {
            itemArray = new JSONArray();
            itemArray.add(itemObject);
        }

        for (int i = 0; i< itemArray.size(); i++) {
            JSONObject findItem = (JSONObject) itemArray.get(i);
            String mntimname = (String) findItem.get("mntiname");
            String mntilistno = String.valueOf((Long) findItem.get("mntilistno"));

            if (mntimname.equals(mountainName) && mntilistno.equals(mountainNum)) {
                item = findItem;
            }
        }

        String image = getMountainImage(mountainNum);

        System.out.println(item);
        DetailMountainDTO detailMountainDTO = new DetailMountainDTO();
        detailMountainDTO.setMntilistno((Long) item.get("mntilistno"));
        detailMountainDTO.setMntiname((String) item.get("mntiname"));
        detailMountainDTO.setMntiadd((String) item.get("mntiadd"));
        detailMountainDTO.setMntidetails((String) item.get("mntidetails"));
        detailMountainDTO.setMntihigh(Double.parseDouble(String.valueOf(item.get("mntihigh"))));


        return detailMountainDTO;
    }

    public String getMountainImage(String mntilistno) throws IOException, ParseException {

        String urlStr = callBackUrl + "mntInfoImgOpenAPI2?" +
                "mntiListNo=" + mntilistno +
                "&ServiceKey=" + serviceKey;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(br);

        System.out.println("\n here \n");
        System.out.println(jsonObject);

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");

        if (body.get("items").equals("")) {
            return "";

        } else {
            JSONObject items = (JSONObject) body.get("items");
            Object itemObject = items.get("item");

            String image = "";

            if (itemObject instanceof JSONObject) { // 단일객체
                JSONObject item = (JSONObject) itemObject;
                image = (String) item.get("imgfilename");
            } else if (itemObject instanceof JSONArray) { // 배열
                JSONArray itemArray = (JSONArray) itemObject;
                JSONObject firstItem = (JSONObject) itemArray.get(0);
                image = (String) firstItem.get("imgfilename");
            }

            System.out.println("iomagesdj");
            System.out.println(image);

            if (image == null) {
                return "";
            } else {
                return "www.forest.go.kr/images/data/down/mountain/" + image;
            }
        }

    }

    public String getCoordinate (String longitude, String latitude) throws IOException, ParseException {

        String urlStr = observeUrl + "/getNearbyMsrstnList?" +
                "sidoName=부산"+
                "&pageNo=1" +
                "&numOfRows=10" +
                "&returnType=json" +
                "&serviceKey=" + observeServiceKey +
                "&ver=1.2";
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
        JSONArray items = (JSONArray) body.get("items");
        JSONObject item = (JSONObject) items.get(0);

        System.out.println("");
        System.out.println(item);

        return "wow";
    }

    public AddressXYDTO changeAddressTOXY (String address) {

        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoAPIKey);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, String.class);

        AddressXYDTO xydto = new AddressXYDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode documents = jsonNode.get("documents");
            if (documents.isArray() && documents.size() > 0) {
                JsonNode location = documents.get(0);
                String x = String.valueOf(location.get("x"));
                String y = String.valueOf(location.get("y"));

                xydto.setLongitude(x);
                xydto.setLatitude(y);
                return xydto;
            } else {
                throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND);
        }
    }

}
