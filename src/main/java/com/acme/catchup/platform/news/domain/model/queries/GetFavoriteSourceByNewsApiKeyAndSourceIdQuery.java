package com.acme.catchup.platform.news.domain.model.queries;

import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;

/**
 * Query for retrieving a favorite source by news API key and source ID.
 *
 * @param newsApiKey news API key value object
 * @param sourceId source identifier value object
 */
public record GetFavoriteSourceByNewsApiKeyAndSourceIdQuery(NewsApiKey newsApiKey, SourceId sourceId) {
    public GetFavoriteSourceByNewsApiKeyAndSourceIdQuery {
        if (newsApiKey == null)
            throw new IllegalArgumentException("newsApiKey cannot be null");
        if (sourceId == null)
            throw new IllegalArgumentException("sourceId cannot be null");
    }
}
