package com.phytoncide.hikinglog.domain.mountain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {

    public String temperature;
    public String rain;
    public String wind;
    public String dust;
}
