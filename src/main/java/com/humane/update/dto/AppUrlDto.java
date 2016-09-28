package com.humane.update.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AppUrlDto {
    private final String clientId;
    private final String name;
    private final String url;

    @QueryProjection
    public AppUrlDto(String clientId, String name, String url) {
        this.clientId = clientId;
        this.name = name;
        this.url = url;
    }
}