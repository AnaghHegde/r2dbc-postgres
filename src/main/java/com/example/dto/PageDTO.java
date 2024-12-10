package com.example.dto;

import com.example.entity.Layout;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PageDTO {
    @Transient
    private String id;
    
    private String name;
    private String icon;
    private String slug;
    private String customSlug;
    private List<Layout> layouts;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant deletedAt = null;

    private Boolean isHidden;
    private Map<String, List<String>> dependencyMap;

    public void sanitiseToExportDBObject() {
        this.setDependencyMap(null);
        if (this.getLayouts() != null) {
            this.getLayouts().forEach(Layout::sanitiseToExportDBObject);
        }
    }
} 