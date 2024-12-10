package com.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("application")
public class Application {
    private static ObjectMapper objectMapper;

    @Autowired
    @PostConstruct
    public void setup(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("workspace_id")
    private String workspaceId;

    @Column("is_public")
    private Boolean isPublic;

    @Column("pages")
    private String pages; // JSON string

    @Column("published_pages")
    private String publishedPages; // JSON string

    @Transient
    private Boolean viewMode = false;

    @Transient
    private boolean appIsExample = false;

    @Column("cloned_from_application_id")
    private String clonedFromApplicationId;

    @Column("color")
    private String color;

    @Column("icon")
    private String icon;

    @Column("slug")
    private String slug;

    @Column("evaluation_version")
    private Integer evaluationVersion;

    @Column("application_version")
    private Integer applicationVersion;

    @Column("last_edited_at")
    private Instant lastEditedAt;

    @Column("last_deployed_at")
    private Instant lastDeployedAt;

    @Column("forking_enabled")
    private Boolean forkingEnabled;

    @Column("is_manual_update")
    private Boolean isManualUpdate;

    @Transient
    private Boolean isAutoUpdate;

    @Column("client_schema_version")
    private Integer clientSchemaVersion;

    @Column("server_schema_version")
    private Integer serverSchemaVersion;

    @Column("published_mode_theme_id")
    private String publishedModeThemeId;

    @Column("edit_mode_theme_id")
    private String editModeThemeId;

    @Column("export_with_configuration")
    private Boolean exportWithConfiguration;

    @Column("fork_with_configuration")
    private Boolean forkWithConfiguration;

    @Column("is_community_template")
    private Boolean isCommunityTemplate;

    @Column("forked_from_template_title")
    private String forkedFromTemplateTitle;

    @Column("collapse_invisible_widgets")
    private Boolean collapseInvisibleWidgets;

    @Column("ssh_key")
    private String sshKey;

    @JsonProperty(value = "modifiedAt", access = JsonProperty.Access.READ_ONLY)
    public String getLastUpdateTime() {
        if (lastEditedAt != null) {
            return lastEditedAt.toString();
        }
        return null;
    }

    public String getLastDeployedAt() {
        if (lastDeployedAt != null) {
            return lastDeployedAt.toString();
        }
        return null;
    }

    public List<ApplicationPage> getPages() {
        try {
            if (this.pages == null) {
                return new ArrayList<>();
            }
            return Boolean.TRUE.equals(viewMode) 
                ? objectMapper.readValue(publishedPages, new TypeReference<List<ApplicationPage>>(){})
                : objectMapper.readValue(pages, new TypeReference<List<ApplicationPage>>(){});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void setPages(List<ApplicationPage> pages) {
        try {
            if (pages == null) {
                this.pages = "[]";
                return;
            }
            this.pages = objectMapper.writeValueAsString(pages);
        } catch (Exception e) {
            this.pages = "[]";
        }
    }

    public void setPublishedPages(List<ApplicationPage> publishedPages) {
        try {
            if (publishedPages == null) {
                this.publishedPages = "[]";
                return;
            }
            this.publishedPages = objectMapper.writeValueAsString(publishedPages);
        } catch (Exception e) {
            this.publishedPages = "[]";
        }
    }
} 