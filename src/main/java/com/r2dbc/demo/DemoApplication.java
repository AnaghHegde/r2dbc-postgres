package com.r2dbc.demo;

import com.example.service.ApplicationService;
import com.example.service.NewPageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ApplicationService applicationService, NewPageService pageService) {
        return args -> {
            // Create application with pages
            applicationService.createApplicationWithPages()
                    .flatMap(app -> {
                        System.out.println("Created application: " + app.getName());
                        
                        // Update application name
                        return applicationService.updateName(app.getId().toString(), "Updated App Name")
                                .flatMap(updatedApp -> {
                                    System.out.println("Updated application name: " + updatedApp.getName());
                                    
                                    // Get pages
                                    return applicationService.getPages(updatedApp.getId().toString())
                                            .flatMap(pages -> {
                                                System.out.println("Application pages: " + pages.size());
                                                
                                                // Update first page name
                                                if (!pages.isEmpty()) {
                                                    return pageService.updateName(pages.get(0).getId(), "Updated Page Name");
                                                }
                                                return null;
                                            });
                                });
                    })
                    .subscribe(
                            result -> System.out.println("Operations completed successfully"),
                            error -> System.err.println("Error occurred: " + error.getMessage())
                    );
        };
    }
}
