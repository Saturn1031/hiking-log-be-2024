package com.phytoncide.hikinglog.domain.mountain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveMountainDTO {

    private Integer mntilistno;
    private String mName;
}
