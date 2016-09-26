package com.humane.update.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AppUrlDto {
    private String name;
    private String url;

    @QueryProjection
    public AppUrlDto(String name, String url) {
        this.name = name;
        this.url = url;
    }
}