package com.acme.catchup.platform.news.domain.model.commands;

import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;

/**
 * Command for creating a favorite news source.
 *
 * @param newsApiKey news API key value object
 * @param sourceId   source identifier value object
 */
public record CreateFavoriteSourceCommand(NewsApiKey newsApiKey, SourceId sourceId) {
    /**
     * Validates the command.
     * @throws IllegalArgumentException If newsApiKey or source ID is null
     */
    public CreateFavoriteSourceCommand {
        if (newsApiKey == null)
            throw new IllegalArgumentException("newsApiKey cannot be null");
        if (sourceId == null)
            throw new IllegalArgumentException("sourceId cannot be null");
    }

}
