package com.phytoncide.hikinglog.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDTO {

    @NotNull
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private Date birth;

    @NotNull
    private String sex;

    @NotNull
    private String phone;

}
