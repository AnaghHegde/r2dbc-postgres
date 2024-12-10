package com.example.service;

import com.example.entity.Application;
import com.example.entity.ApplicationPage;
import com.example.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public Mono<Application> findById(String id) {
        return applicationRepository.findById(id);
    }

    public Mono<Application> findByName(String name) {
        return applicationRepository.findByName(name);
    }

    public Flux<Application> findByWorkspaceId(String workspaceId) {
        return applicationRepository.findByWorkspaceId(workspaceId);
    }

    public Mono<Application> findByIdAndWorkspaceId(String id, String workspaceId) {
        return applicationRepository.findByIdAndWorkspaceIdCustom(id, workspaceId);
    }

    public Mono<Application> createApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Mono<Application> updateApplication(String id, Application application) {
        return applicationRepository.findById(id)
                .flatMap(existingApp -> {
                    application.setId(Long.valueOf(id));
                    return applicationRepository.save(application);
                });
    }

    public Mono<Application> addPage(String applicationId, ApplicationPage page) {
        return applicationRepository.findById(applicationId)
                .flatMap(application -> {
                    List<ApplicationPage> pages = application.getPages();
                    if (pages == null) {
                        pages = new ArrayList<>();
                    }
                    pages.add(page);
                    application.setPages(pages);
                    return applicationRepository.save(application);
                });
    }

    public Mono<Application> removePage(String applicationId, String pageId) {
        return applicationRepository.findById(applicationId)
                .flatMap(application -> {
                    List<ApplicationPage> pages = application.getPages();
                    if (pages != null) {
                        pages.removeIf(page -> page.getId().equals(pageId));
                        application.setPages(pages);
                    }
                    return applicationRepository.save(application);
                });
    }

    public Mono<List<ApplicationPage>> getPages(String applicationId) {
        return applicationRepository.findById(applicationId)
                .map(Application::getPages);
    }

    public Mono<ApplicationPage> getDefaultPage(String applicationId) {
        return getPages(applicationId)
                .map(pages -> pages.stream()
                        .filter(ApplicationPage::isDefault)
                        .findFirst()
                        .orElse(null));
    }

    public Mono<Application> updateName(String id, String newName) {
        return applicationRepository.findById(id)
                .flatMap(application -> {
                    application.setName(newName);
                    application.setLastEditedAt(Instant.now());
                    return applicationRepository.save(application);
                });
    }

    @Transactional
    public Mono<Application> createApplicationWithPages() {
        String randomName = "App_" + UUID.randomUUID().toString().substring(0, 8);
        
        // Create application with default values
        Application application = new Application();
        application.setName(randomName);
        application.setWorkspaceId("default-workspace");
        application.setIsPublic(false);
        application.setColor("#FF0000");
        application.setIcon("rocket");
        application.setSlug(randomName.toLowerCase());
        application.setEvaluationVersion(1);
        application.setApplicationVersion(1);
        application.setLastEditedAt(Instant.now());
        application.setClientSchemaVersion(1);
        application.setServerSchemaVersion(1);
        application.setForkingEnabled(true);
        application.setIsManualUpdate(false);
        application.setIsAutoUpdate(false);

        // Create three pages
        List<ApplicationPage> pages = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            ApplicationPage page = new ApplicationPage();
            page.setId(UUID.randomUUID().toString());
            page.setIsDefault(i == 1); // First page is default
            page.setSlug("page-" + i);
            page.setCustomSlug("custom-page-" + i);
            pages.add(page);
        }

        // Create JSON structure for pages
        application.setPages(pages);
        
        // Create similar structure for published pages
        application.setPublishedPages(pages);

        // Save application and update pages
        return applicationRepository.save(application)
                .flatMap(savedApp -> {
                    // Update application with page references
                    List<ApplicationPage> savedPages = savedApp.getPages();
                    if (savedPages != null) {
                        savedPages.forEach(page -> {
                            page.setDefaultPageId(page.getId());
                        });
                        savedApp.setPages(savedPages);
                        savedApp.setPublishedPages(savedPages);
                        return applicationRepository.save(savedApp);
                    }
                    return Mono.just(savedApp);
                });
    }

    // Helper method to generate default JSON for pages
    private String generateDefaultPageJson(ApplicationPage page) {
        return String.format("""
            {
                "id": "%s",
                "isDefault": %b,
                "slug": "%s",
                "customSlug": "%s",
                "defaultPageId": "%s"
            }
            """, 
            page.getId(),
            page.isDefault(),
            page.getSlug(),
            page.getCustomSlug(),
            page.getDefaultPageId()
        );
    }
} 