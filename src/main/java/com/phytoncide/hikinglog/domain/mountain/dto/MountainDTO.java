package com.phytoncide.hikinglog.domain.mountain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MountainDTO {

    private String mName;
    private String location;
    private String info;
    private String mImage;
    private String level;
    private String features;

}
