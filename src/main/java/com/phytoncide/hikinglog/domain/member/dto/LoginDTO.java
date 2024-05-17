package com.phytoncide.hikinglog.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
