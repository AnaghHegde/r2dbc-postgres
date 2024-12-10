package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ApplicationPage {

    @Id
    private String id;

    private Boolean isDefault;

    @Transient
    private String slug;

    @Transient
    private String customSlug;

    private String defaultPageId;

    @JsonIgnore
    public boolean isDefault() {
        return Boolean.TRUE.equals(isDefault);
    }

    @Transient
    public String getBaseId() {
        return StringUtils.hasLength(defaultPageId) ? defaultPageId : id;
    }
}
