package com.acme.catchup.platform.news.domain.model.valueobjects;

import java.util.regex.Pattern;

/**
 * Value object representing a News API key in the domain model.
 *
 * @param value raw API key value
 */
public record NewsApiKey(String value) {
    private static final int MAX_LENGTH = 256;
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[A-Za-z0-9._:-]+$");

    public NewsApiKey {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("newsApiKey cannot be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("newsApiKey cannot exceed 256 characters");
        }
        if (!ALLOWED_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("newsApiKey contains invalid characters");
        }
    }
}

