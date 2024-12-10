CREATE TABLE new_page (
    id VARCHAR(255) PRIMARY KEY,
    application_id VARCHAR(255) NOT NULL,
    unpublished_page JSONB,
    published_page JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_new_page_application_id ON new_page(application_id);
CREATE INDEX idx_new_page_deleted_at ON new_page(deleted_at) WHERE deleted_at IS NOT NULL; 