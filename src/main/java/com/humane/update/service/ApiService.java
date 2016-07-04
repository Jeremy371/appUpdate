package com.humane.update.service;

import com.humane.update.dto.AppUrlDto;
import com.humane.update.dto.AppVersionDto;
import com.humane.update.model.QAppUrl;
import com.humane.update.model.QAppVersion;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ApiService {
    @PersistenceContext private EntityManager entityManager;

    public AppVersionDto getLastVersion(String packageName) {
        QAppVersion appVersion = QAppVersion.appVersion;

        return new JPAQuery(entityManager)
                .from(appVersion)
                .where(appVersion.app.packageName.eq(packageName),
                        appVersion.isUse.eq(true)
                ).orderBy(appVersion.regDttm.desc())
                .limit(1)
                .uniqueResult(
                        ConstructorExpression.create(AppVersionDto.class,
                                appVersion.app.packageName,
                                appVersion.versionCode,
                                appVersion.versionName,
                                appVersion.message)
                );
    }

    public List<AppUrlDto> getUrlList(String packageName) {
        QAppUrl appUrl = QAppUrl.appUrl;

        return new JPAQuery(entityManager)
                .from(appUrl)
                .where(appUrl.app.packageName.eq(packageName),
                        appUrl.isUse.eq(true))
                .list(
                        ConstructorExpression.create(AppUrlDto.class,
                                appUrl.name,
                                appUrl.url)
                );
    }
}
