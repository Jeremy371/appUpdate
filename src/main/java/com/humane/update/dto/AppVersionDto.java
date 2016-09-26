package com.humane.update.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AppVersionDto {
    private String packageName;
    private Long versionCode;
    private String versionName;
    private String message;

    @QueryProjection
    public AppVersionDto(String packageName, Long versionCode, String versionName, String message) {
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.message = message;
    }
}