package com.phytoncide.hikinglog.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {

    @NotNull
    private String originPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String newPassword;
}
