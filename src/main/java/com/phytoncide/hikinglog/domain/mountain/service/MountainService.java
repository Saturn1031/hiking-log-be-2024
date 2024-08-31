package com.phytoncide.hikinglog.domain.mountain.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.dto.MountainDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.WeatherDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.store.dto.AccomoDetailResponseDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class MountainService {
    private final MountainRepository mountainRepository;
    private final HttpServletRequest httpServletRequest;

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

    @Autowired
    public MountainService(MountainRepository mountainRepository, HttpServletRequest httpServletRequest) {
        this.mountainRepository = mountainRepository;
        this.httpServletRequest = httpServletRequest;
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
        JSONObject result = (JSONObject) response.get("result");
        JSONObject featureCollection = (JSONObject) result.get("featureCollection");
        JSONArray features = (JSONArray) featureCollection.get("features");

        ArrayList<ArrayList<Float>> trail = new ArrayList<ArrayList<Float>>();

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

        String urlStr = weatherUrl + "/getUltraSrtNcst?" +
                "&serviceKey=" + weatherServiceKey +
                "&numOfRows=20" +
                "&pageNo=1" +
                "&dataType=JSON" +
                "&base_date="+ formattedDate +
                "&base_time=0600" +
                "&nx=55"+
                "&ny=127";
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

    public SaveMountainDTO searchMountain(String mountainName) throws IOException, ParseException {

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
        JSONObject items = (JSONObject) body.get("items");
        JSONObject item = (JSONObject) items.get("item");

        SaveMountainDTO saveMountainDTO = new SaveMountainDTO();
        saveMountainDTO.setMntilistno(Integer.parseInt(String.valueOf((Long) item.get("mntilistno"))));
        saveMountainDTO.setMName((String) item.get("mntiname"));
        saveMountainDTO.setLocation((String) item.get("mntiadd"));
        saveMountainDTO.setInfo((String) item.get("mntidetails"));
        saveMountainDTO.setMntiHigh((Double) item.get("mntihigh"));
        saveMountainDTO.setMImage((String) item.get("mntitop"));



        return saveMountainDTO;
    }
}
