package com.acme.catchup.platform.news.domain.model.valueobjects;

import java.util.regex.Pattern;

/**
 * Value object representing a news source identifier in the domain model.
 *
 * @param value raw source identifier value
 */
public record SourceId(String value) {
    private static final int MAX_LENGTH = 256;
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[A-Za-z0-9._:-]+$");

    public SourceId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("sourceId cannot be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("sourceId cannot exceed 256 characters");
        }
        if (!ALLOWED_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("sourceId contains invalid characters");
        }
    }
}

