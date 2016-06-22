package com.humane.update.dto;

import com.mysema.query.annotations.QueryProjection;
import lombok.Data;

@Data
public class AppVersionDto {
    private String packageName;
    private Long versionCode;
    private String versionName;

    @QueryProjection
    public AppVersionDto(String packageName, Long versionCode, String versionName) {
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }
}