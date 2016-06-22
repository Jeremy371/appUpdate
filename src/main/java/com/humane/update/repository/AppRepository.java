package com.humane.update.repository;

import com.humane.update.model.App;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends QueryDslJpaExtendRepository<App, Long> {
}