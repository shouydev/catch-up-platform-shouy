package com.acme.catchup.platform.news.domain.model.queries;

import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;

/**
 * Query for retrieving all favorite sources by news API key.
 *
 * @param newsApiKey news API key value object used to filter favorite sources
 */
public record GetAllFavoriteSourcesByNewsApiKeyQuery(NewsApiKey newsApiKey) {
    public GetAllFavoriteSourcesByNewsApiKeyQuery {
        if (newsApiKey == null)
            throw new IllegalArgumentException("newsApiKey cannot be null");

    }
}

