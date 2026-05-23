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
    private static final String NOT_BLANK_MESSAGE_KEY = "favorite.source.error.newsApiKey.notBlank";
    private static final String SIZE_MESSAGE_KEY = "favorite.source.error.newsApiKey.size";
    private static final String PATTERN_MESSAGE_KEY = "favorite.source.error.newsApiKey.pattern";

    public NewsApiKey {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(NOT_BLANK_MESSAGE_KEY);
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(SIZE_MESSAGE_KEY);
        }
        if (!ALLOWED_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(PATTERN_MESSAGE_KEY);
        }
    }
}

