package com.humane.update.service;

import com.humane.update.dto.AppUrlDto;
import com.humane.update.dto.AppVersionDto;
import com.humane.update.model.QAppUrl;
import com.humane.update.model.QAppVersion;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ApiService {
    @PersistenceContext private EntityManager entityManager;

    public List<AppVersionDto> getLastVersion(String packageName) {
        QAppVersion appVersion = QAppVersion.appVersion;

        ConstructorExpression<AppVersionDto> constructor = Projections.constructor(AppVersionDto.class, appVersion.app.packageName, appVersion.versionCode, appVersion.versionName, appVersion.message);

        //return
        JPAQuery<AppVersionDto> jpaQuery = new JPAQuery<>(entityManager)
                .select(constructor)
                .from(appVersion)
                .where(appVersion.app.packageName.eq(packageName), appVersion.isUse.eq(true));

        return jpaQuery.fetch();
    }


    public List<AppUrlDto> getUrlList(String packageName) {
        QAppUrl appUrl = QAppUrl.appUrl;

        ConstructorExpression<AppUrlDto> constructor = Projections.constructor(AppUrlDto.class, appUrl.clientId, appUrl.name, appUrl.url);

        return new JPAQuery<>(entityManager)
                .select(constructor)
                .from(appUrl)
                .where(appUrl.app.packageName.eq(packageName), appUrl.isUse.eq(true))
                .fetch();
    }
}
