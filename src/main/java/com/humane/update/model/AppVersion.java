package com.humane.update.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"appId", "versionCode"})})
@DynamicInsert
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AppVersion {
    @Id @GeneratedValue private Long _id;
    @Column(nullable = false) private Long versionCode;
    @Column(nullable = false) private String versionName;
    @ManyToOne @JoinColumn(name = "appId", nullable = false) private App app;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(columnDefinition = "datetime default current_timestamp") private Date regDttm;
    @Column(columnDefinition = "bit default 1") private Boolean isUse;
}
