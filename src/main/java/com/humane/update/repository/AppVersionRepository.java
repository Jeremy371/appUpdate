package com.humane.update.repository;

import com.humane.update.model.AppVersion;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepository extends QueryDslJpaExtendRepository<AppVersion, Long> {
}