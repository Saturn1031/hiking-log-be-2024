package com.phytoncide.hikinglog.domain.mountain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailMountainDTO {

    private String mntiadd;
    private String mntiadmin;
    private String mntiadminnum;
    private String mntidetails;
    private Double mntihigh;
    private Long mntilistno;
    private String mntiname;
    private Long mntinfdt;
    private String mntisname;
    private String mntisummary;
    private String mntitop;

}
