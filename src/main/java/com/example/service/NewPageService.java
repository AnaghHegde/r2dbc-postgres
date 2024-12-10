package com.example.service;

import com.example.entity.NewPage;
import com.example.dto.PageDTO;
import com.example.repository.NewPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NewPageService {
    private final NewPageRepository newPageRepository;

    public Mono<NewPage> findById(String id) {
        return newPageRepository.findById(id);
    }

    public Mono<NewPage> findByName(String name) {
        return newPageRepository.findByName(name);
    }

    public Flux<NewPage> findByApplicationId(String applicationId) {
        return newPageRepository.findByApplicationId(applicationId);
    }

    public Flux<NewPage> findByApplicationIdAndWorkspaceId(String applicationId, String workspaceId) {
        return newPageRepository.findByApplicationIdAndWorkspaceId(applicationId, workspaceId);
    }

    public Mono<NewPage> findByIdAndApplicationId(String id, String applicationId) {
        return newPageRepository.findByIdAndApplicationId(id, applicationId);
    }

    public Mono<NewPage> createPage(NewPage page) {
        return newPageRepository.save(page);
    }

    public Mono<NewPage> updatePage(String id, NewPage page) {
        return newPageRepository.findById(id)
                .flatMap(existingPage -> {
                    page.setId(id);
                    return newPageRepository.save(page);
                });
    }

    public Mono<NewPage> updateName(String id, String newName) {
        return newPageRepository.findById(id)
                .flatMap(page -> {
                    // Update unpublished page name
                    if (page.getUnpublishedPage() != null) {
                        page.getUnpublishedPage().setName(newName);
                    } else {
                        PageDTO unpublishedPage = new PageDTO();
                        unpublishedPage.setName(newName);
                        page.setUnpublishedPage(unpublishedPage);
                    }

                    // Update published page name if it exists
                    if (page.getPublishedPage() != null) {
                        page.getPublishedPage().setName(newName);
                    }

                    return newPageRepository.save(page);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Page not found with id: " + id)));
    }
} 