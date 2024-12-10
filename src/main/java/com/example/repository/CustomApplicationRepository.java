package com.example.repository;

import com.example.entity.Application;
import reactor.core.publisher.Mono;

public interface CustomApplicationRepository {
    Mono<Application> findByIdAndWorkspaceIdCustom(String id, String workspaceId);
} 