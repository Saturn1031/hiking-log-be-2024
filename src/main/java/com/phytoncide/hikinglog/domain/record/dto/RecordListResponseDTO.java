package com.phytoncide.hikinglog.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordListResponseDTO {

    @NotNull
    private String mName;

    @NotNull
    private long number;

    @NotNull
    private long time;

    @NotNull
    private LocalDate date;
}
