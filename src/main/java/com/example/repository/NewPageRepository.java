package com.example.repository;

import com.example.entity.NewPage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NewPageRepository extends R2dbcRepository<NewPage, String> {
    Mono<NewPage> findByIdAndApplicationId(String id, String applicationId);
    
    Flux<NewPage> findByApplicationId(String applicationId);
    
    @Query("SELECT * FROM new_page WHERE application_id = :applicationId AND workspace_id = :workspaceId")
    Flux<NewPage> findByApplicationIdAndWorkspaceId(String applicationId, String workspaceId);
    
    Mono<NewPage> findByName(String name);
} 