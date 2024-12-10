package com.example.repository;

import com.example.entity.Application;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;

public class CustomApplicationRepositoryImpl implements CustomApplicationRepository {
    private final R2dbcEntityTemplate template;

    public CustomApplicationRepositoryImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Application> findByIdAndWorkspaceIdCustom(String id, String workspaceId) {
        return template.select(Application.class)
                .matching(Query.query(Criteria.where("id").is(id)
                        .and("workspace_id").is(workspaceId)))
                .one();
    }
} 