package com.phytoncide.hikinglog.domain.mountain.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.RegisterException;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.mountain.dto.MountainDTO;
import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
