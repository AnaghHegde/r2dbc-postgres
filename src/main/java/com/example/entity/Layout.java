package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Layout {
    private String id;
    private String name;
    
    public void sanitiseToExportDBObject() {
        // Implementation for sanitization
    }
} 