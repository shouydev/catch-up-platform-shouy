package com.acme.catchup.platform.news.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a favorite source.
 *
 * @param id persistent identifier
 * @param newsApiKey news API key associated with the favorite source
 * @param sourceId source identifier marked as favorite
 * @since 1.0
 */
@Schema(description = "Response resource representing a favorite source")
public record FavoriteSourceResource(
		@Schema(description = "Persistent identifier", example = "1")
		Long id,
		@Schema(description = "News API key associated with the favorite source", example = "news-api-key-123")
		String newsApiKey,
		@Schema(description = "Source identifier marked as favorite", example = "bbc-news")
		String sourceId) {
}
