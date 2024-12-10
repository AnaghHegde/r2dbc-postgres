package com.example.service;

import com.example.entity.Application;
import com.example.entity.ApplicationPage;
import com.example.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createApplicationWithPages_Success() {
        Application mockApp = new Application();
        mockApp.setId(1L);
        mockApp.setName("Test App");
        
        when(applicationRepository.save(any(Application.class)))
                .thenReturn(Mono.just(mockApp));

        StepVerifier.create(applicationService.createApplicationWithPages())
                .expectNextMatches(app -> 
                    app.getId().equals(1L) &&
                    app.getName().equals("Test App")
                )
                .verifyComplete();
    }

    @Test
    void updateName_Success() {
        Application existingApp = new Application();
        existingApp.setId(1L);
        existingApp.setName("Old Name");

        Application updatedApp = new Application();
        updatedApp.setId(1L);
        updatedApp.setName("New Name");

        when(applicationRepository.findById("1"))
                .thenReturn(Mono.just(existingApp));
        when(applicationRepository.save(any(Application.class)))
                .thenReturn(Mono.just(updatedApp));

        StepVerifier.create(applicationService.updateName("1", "New Name"))
                .expectNextMatches(app -> 
                    app.getName().equals("New Name")
                )
                .verifyComplete();
    }
} 