package com.example.repository;

import com.example.entity.Application;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationRepository extends R2dbcRepository<Application, String>, CustomApplicationRepository {
    Mono<Application> findByName(String name);
    
    Flux<Application> findByWorkspaceId(String workspaceId);
    
    @Query("SELECT * FROM application WHERE id = :id AND workspace_id = :workspaceId")
    Mono<Application> findByIdAndWorkspaceId(String id, String workspaceId);
} 