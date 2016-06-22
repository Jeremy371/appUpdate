package com.humane.util.spring.data;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


/**
 * Spring Data JPA Tutorial: Adding Custom Methods Into All Repositories
 * http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-adding-custom-methods-into-all-repositories/
 * <p>
 * Spring Data JPA and Querydsl to fetch subset of columns using bean/constructor projection
 * http://stackoverflow.com/a/18313290
 * <p>
 * Is there a way to eager fetch a lazy relationship through the Predicate API in QueryDSL?
 * http://stackoverflow.com/a/21630123
 */
@NoRepositoryBean
public interface QueryDslJpaExtendRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {
    T findOne(Predicate predicate, JoinDescriptor... joinDescriptors);

    Page<T> findAll(EntityPath<T> entityPath, Predicate predicate, Pageable pageable);

    Page<T> findAll(Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors);

    Page<T> findAll(EntityPath<T> entityPath, Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors);
}