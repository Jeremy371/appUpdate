package com.humane.util.spring.data;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class QueryDslJpaExtendRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements QueryDslJpaExtendRepository<T, ID> {

    //All instance variables are available in super, but they are private
    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    private final EntityPath<T> path;
    private final PathBuilder<T> builder;
    private final Querydsl querydsl;

    public QueryDslJpaExtendRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    public QueryDslJpaExtendRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {
        super(entityInformation, entityManager);
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @Override
    public T findOne(Predicate predicate, JoinDescriptor... joinDescriptors) {
        JPQLQuery query = createFetchQuery(predicate, joinDescriptors);
        return query.uniqueResult(path);
    }

    @Override
    public Page<T> findAll(EntityPath<T> entityPath, Predicate predicate, Pageable pageable) {
        JPQLQuery countQuery = createQuery(predicate);
        JPQLQuery query = querydsl.applyPagination(pageable, createQuery(predicate));

        Long total = countQuery.count();
        List<T> content = total > pageable.getOffset() ? query.list(entityPath) : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors) {
        JPQLQuery countQuery = createQuery(predicate);
        JPQLQuery query = querydsl.applyPagination(pageable, createFetchQuery(predicate, joinDescriptors));

        Long total = countQuery.count();
        List<T> content = total > pageable.getOffset() ? query.list(path) : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<T> findAll(EntityPath<T> entityPath, Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors) {
        JPQLQuery countQuery = createQuery(predicate);
        JPQLQuery query = querydsl.applyPagination(pageable, createFetchQuery(predicate, joinDescriptors));

        Long total = countQuery.count();
        List<T> content = total > pageable.getOffset() ? query.list(entityPath) : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    private JPQLQuery createFetchQuery(Predicate predicate, JoinDescriptor... joinDescriptors) {
        JPQLQuery query = querydsl.createQuery(path);
        for (JoinDescriptor joinDescriptor : joinDescriptors)
            join(joinDescriptor, query);
        return query.where(predicate);
    }

    private JPQLQuery join(JoinDescriptor joinDescriptor, JPQLQuery query) {
        switch (joinDescriptor.type) {
            case DEFAULT:
                throw new IllegalArgumentException("cross join not supported");
            case INNERJOIN:
                query.innerJoin(joinDescriptor.path);
                break;
            case JOIN:
                query.join(joinDescriptor.path);
                break;
            case LEFTJOIN:
                query.leftJoin(joinDescriptor.path);
                break;
            case RIGHTJOIN:
                query.rightJoin(joinDescriptor.path);
                break;
            case FULLJOIN:
                query.fullJoin(joinDescriptor.path);
                break;
        }
        return query.fetch();
    }
}