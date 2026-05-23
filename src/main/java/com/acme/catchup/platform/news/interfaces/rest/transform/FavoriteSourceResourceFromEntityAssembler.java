package com.acme.catchup.platform.news.interfaces.rest.transform;

import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.interfaces.rest.resources.FavoriteSourceResource;

/**
 * Interface layer translator converting domain aggregates to HTTP outbound Resources.
 * Crosses the boundary between the domain model ({@link FavoriteSource}) and external
 * representation ({@link FavoriteSourceResource}), enabling the inbound service
 * to serialize domain responses into client-facing HTTP representations.
 *
 * @since 1.0
 */
public class FavoriteSourceResourceFromEntityAssembler {
    /**
     * Converts a FavoriteSource entity to a FavoriteSourceResource.
     * @param entity FavoriteSource entity to convert
     * @return FavoriteSourceResource created from the entity
     */
    public static FavoriteSourceResource toResourceFromEntity(FavoriteSource entity) {
        return new FavoriteSourceResource(
                entity.getId(),
                entity.getNewsApiKey().value(),
                entity.getSourceId().value());
    }
}
