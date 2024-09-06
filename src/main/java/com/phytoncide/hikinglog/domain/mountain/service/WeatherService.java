package com.phytoncide.hikinglog.domain.mountain.service;

import com.phytoncide.hikinglog.domain.mountain.dto.WeatherXYDTO;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private static final double PI = Math.asin(1.0) * 2.0;
    private static final double DEGRAD = PI / 180.0;
    private static final double RADDEG = 180.0 / PI;

    private static final int NX = 149; // X축 격자점 수
    private static final int NY = 253; // Y축 격자점 수

    // LCC (Lambert Conformal Conic) Parameters
    private static final double RE = 6371.00877; // 지구 반경 (km)
    private static final double GRID = 5.0; // 격자 간격 (km)
    private static final double SLAT1 = 30.0; // 표준 위도 1
    private static final double SLAT2 = 60.0; // 표준 위도 2
    private static final double OLON = 126.0; // 기준점 경도
    private static final double OLAT = 38.0; // 기준점 위도
    private static final double XO = 210 / GRID; // 기준점 X좌표
    private static final double YO = 675 / GRID; // 기준점 Y좌표

    public WeatherXYDTO getWeatherXY(String lonStr, String latStr) {
        lonStr = lonStr.trim().replace("\"", "");
        latStr = latStr.trim().replace("\"", "");
        System.out.println(lonStr);
        System.out.println(latStr);
        double lon = Double.parseDouble(lonStr);
        double lat = Double.parseDouble(latStr);

        double[] gridXY = lamcproj(lon, lat, true);
        int x = (int) (gridXY[0] + 1.5);
        int y = (int) (gridXY[1] + 1.5);

        WeatherXYDTO weatherXYDTO = new WeatherXYDTO();
        weatherXYDTO.setX(String.valueOf(x));
        weatherXYDTO.setY(String.valueOf(y));

        return weatherXYDTO;
    }

    private double[] lamcproj(double lon, double lat, boolean toGrid) {
        double sn, sf, ro, ra, theta;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        sn = Math.tan(PI * 0.25 + slat2 * 0.5) / Math.tan(PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        sf = Math.tan(PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        ro = Math.tan(PI * 0.25 + olat * 0.5);
        ro = RE / GRID * sf / Math.pow(ro, sn);

        double[] result = new double[2];

        if (toGrid) { // 경위도 -> 격자
            ra = Math.tan(PI * 0.25 + lat * DEGRAD * 0.5);
            ra = RE / GRID * sf / Math.pow(ra, sn);
            theta = lon * DEGRAD - olon;

            if (theta > PI) theta -= 2.0 * PI;
            if (theta < -PI) theta += 2.0 * PI;

            theta *= sn;

            result[0] = ra * Math.sin(theta) + XO;
            result[1] = ro - ra * Math.cos(theta) + YO;
        } else { // 격자 -> 경위도
            double xn = lon - XO;
            double yn = ro - lat + YO;
            ra = Math.sqrt(xn * xn + yn * yn);
            double alat = Math.pow((RE / GRID * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - PI * 0.5;

            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            } else {
                theta = Math.atan2(xn, yn);
            }

            double alon = theta / sn + olon;

            result[0] = alon * RADDEG;
            result[1] = alat * RADDEG;
        }

        return result;
    }
}
