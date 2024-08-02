package com.phytoncide.hikinglog.domain.record.dto;

import com.phytoncide.hikinglog.domain.mountain.dto.SaveMountainDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequestWrapper {
    private RecordRequestDTO recordRequestDTO;
    private SaveMountainDTO saveMountainDTO;
}
