package com.phytoncide.hikinglog.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantListResponseDTO {

    public String name;

    public String contentId;

    public String add;

    public String img;

    public String img2;

    public String mapX;

    public String mapY;

    public String tel;

}
