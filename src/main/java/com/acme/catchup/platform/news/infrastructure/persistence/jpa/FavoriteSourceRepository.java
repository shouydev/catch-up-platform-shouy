package com.acme.catchup.platform.news.infrastructure.persistence.jpa;

import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for querying and persisting {@link FavoriteSource} aggregates.
 * Provides collection-like access to FavoriteSource roots, supporting queries
 * essential to maintaining domain invariants:
 * - {@code findByNewsApiKeyAndSourceId()} checks the unique (newsApiKey, sourceId) invariant
 * - {@code findAllByNewsApiKey()} retrieves all favorites for a user's API context
 * - {@code existsByNewsApiKeyAndSourceId()} tests invariant satisfaction
 *
 * @since 1.0
 */
@Repository
public interface FavoriteSourceRepository extends JpaRepository<FavoriteSource, Long> {
    /**
     * Find all favorite sources by news API key.
     * @param newsApiKey News API key
     * @return List of favorite sources
     */
    List<FavoriteSource> findAllByNewsApiKey(NewsApiKey newsApiKey);

    /**
     * Check if a favorite source exists by news API key and source ID.
     * @param newsApiKey News API key
     * @param sourceId Source ID
     * @return True if exists, false otherwise
     */
    boolean existsByNewsApiKeyAndSourceId(NewsApiKey newsApiKey, SourceId sourceId);

    /**
     * Find a favorite source by news API key and source ID.
     * @param newsApiKey News API key
     * @param sourceId Source ID
     * @return Favorite source
     */
    Optional<FavoriteSource> findByNewsApiKeyAndSourceId(NewsApiKey newsApiKey, SourceId sourceId);
}
