package com.example.service;

import com.example.dto.PageDTO;
import com.example.entity.NewPage;
import com.example.repository.NewPageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class NewPageServiceTest {

    @Mock
    private NewPageRepository newPageRepository;

    @InjectMocks
    private NewPageService newPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateName_Success() {
        NewPage existingPage = new NewPage();
        existingPage.setId("1");
        
        PageDTO unpublishedPage = new PageDTO();
        unpublishedPage.setName("Old Name");
        existingPage.setUnpublishedPage(unpublishedPage);

        NewPage updatedPage = new NewPage();
        updatedPage.setId("1");
        PageDTO updatedUnpublishedPage = new PageDTO();
        updatedUnpublishedPage.setName("New Name");
        updatedPage.setUnpublishedPage(updatedUnpublishedPage);

        when(newPageRepository.findById("1"))
                .thenReturn(Mono.just(existingPage));
        when(newPageRepository.save(any(NewPage.class)))
                .thenReturn(Mono.just(updatedPage));

        StepVerifier.create(newPageService.updateName("1", "New Name"))
                .expectNextMatches(page -> 
                    page.getUnpublishedPage().getName().equals("New Name")
                )
                .verifyComplete();
    }

    @Test
    void updateName_PageNotFound() {
        when(newPageRepository.findById("1"))
                .thenReturn(Mono.empty());

        StepVerifier.create(newPageService.updateName("1", "New Name"))
                .expectError(RuntimeException.class)
                .verify();
    }
} 