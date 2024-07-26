package com.phytoncide.hikinglog.domain.record.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.Duration;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequestDTO {

    @NotNull
    private String email;

    @NotNull
    private LocalDate date;

    @NotNull
    private Duration time;
}
