package com.acme.catchup.platform.news.interfaces.rest.transform;

import com.acme.catchup.platform.news.domain.model.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.interfaces.rest.resources.CreateFavoriteSourceResource;

/**
 * Interface layer translator converting HTTP inbound Resources to domain commands.
 * Crosses the boundary between external representation ({@link CreateFavoriteSourceResource})
 * and domain model ({@link CreateFavoriteSourceCommand}), enabling the inbound service
 * to translate client input into domain-ready operations for the application layer.
 *
 * @since 1.0
 */
public class CreateFavoriteSourceCommandFromResourceAssembler {
    /**
     * Converts a CreateFavoriteSourceResource to a CreateFavoriteSourceCommand.
     * @param resource CreateFavoriteSourceResource to convert
     * @return CreateFavoriteSourceCommand created from the resource
     */
    public static CreateFavoriteSourceCommand toCommandFromResource(CreateFavoriteSourceResource resource) {
        return new CreateFavoriteSourceCommand(
                NewsValueObjectFromStringAssembler.toNewsApiKeyFromString(resource.newsApiKey()),
                NewsValueObjectFromStringAssembler.toSourceIdFromString(resource.sourceId()));
    }
}
