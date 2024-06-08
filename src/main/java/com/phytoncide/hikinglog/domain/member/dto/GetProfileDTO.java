package com.phytoncide.hikinglog.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileDTO {

    private String email;

    private String name;

    private Date birth;

    private String sex;

    private String phone;

    private String image;
}
