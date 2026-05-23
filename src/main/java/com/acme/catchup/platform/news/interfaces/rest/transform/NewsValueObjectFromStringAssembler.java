package com.acme.catchup.platform.news.interfaces.rest.transform;

import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;

/**
 * Interface layer translator converting string values from HTTP inputs into domain value objects.
 *
 * @since 1.0
 */
public class NewsValueObjectFromStringAssembler {
    /**
     * Converts a raw string into a {@link NewsApiKey} value object.
     *
     * @param value raw API key value
     * @return validated news API key value object
     */
    public static NewsApiKey toNewsApiKeyFromString(String value) {
        return new NewsApiKey(value);
    }

    /**
     * Converts a raw string into a {@link SourceId} value object.
     *
     * @param value raw source identifier value
     * @return validated source identifier value object
     */
    public static SourceId toSourceIdFromString(String value) {
        return new SourceId(value);
    }
}

