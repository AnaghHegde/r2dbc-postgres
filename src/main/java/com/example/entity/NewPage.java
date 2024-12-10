package com.example.entity;

import com.example.dto.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table("new_page")
public class NewPage {
    @Id
    private String id;

    @Column("application_id")
    private String applicationId;

    @Column("unpublished_page")
    private PageDTO unpublishedPage;

    @Column("published_page")
    private PageDTO publishedPage;

    public void sanitiseToExportDBObject() {
        this.setApplicationId(null);
        this.setId(null);
        if (this.getUnpublishedPage() != null) {
            this.getUnpublishedPage().sanitiseToExportDBObject();
        }
        if (this.getPublishedPage() != null) {
            this.getPublishedPage().sanitiseToExportDBObject();
        }
    }

    public Layout getLayout() {
        if (this.getUnpublishedPage() == null || this.getUnpublishedPage().getLayouts() == null) {
            return null;
        }
        List<Layout> layouts = this.getUnpublishedPage().getLayouts();
        return !layouts.isEmpty() ? layouts.get(0) : null;
    }

    public String getUnpublishedName() {
        if (this.getUnpublishedPage() == null) {
            return null;
        }
        return this.getUnpublishedPage().getName();
    }
} 