package com.phytoncide.hikinglog.domain.mountain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTrailDTO {
    @NotNull
    private String mntiname;

    @NotNull
    private String mntiadd;
}
