package com.acme.catchup.platform.news.application.internal.queryservices;

import com.acme.catchup.platform.news.application.queryservices.FavoriteSourceQueryService;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.queries.GetAllFavoriteSourcesByNewsApiKeyQuery;
import com.acme.catchup.platform.news.domain.model.queries.GetFavoriteSourceByIdQuery;
import com.acme.catchup.platform.news.domain.model.queries.GetFavoriteSourceByNewsApiKeyAndSourceIdQuery;
import com.acme.catchup.platform.news.infrastructure.persistence.jpa.FavoriteSourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service for favorite source query operations.
 * Contains query handler methods that translate domain query specifications
 * into persistence operations, returning domain models from storage.
 *
 * @since 1.0
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class FavoriteSourceQueryServiceImpl implements FavoriteSourceQueryService {
    private final FavoriteSourceRepository favoriteSourceRepository;

    public FavoriteSourceQueryServiceImpl(FavoriteSourceRepository favoriteSourceRepository) {
        this.favoriteSourceRepository = favoriteSourceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FavoriteSource> handle(GetAllFavoriteSourcesByNewsApiKeyQuery query) {
        log.debug("Querying all favorite sources for newsApiKey={}", mask(query.newsApiKey().value()));
        var results = favoriteSourceRepository.findAllByNewsApiKey(query.newsApiKey());
        log.debug("Found {} favorite source(s) for newsApiKey={}", results.size(), mask(query.newsApiKey().value()));
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FavoriteSource> handle(GetFavoriteSourceByIdQuery query) {
        log.debug("Querying favorite source by id={}", query.id());
        var result = favoriteSourceRepository.findById(query.id());
        if (result.isEmpty()) log.debug("No favorite source found for id={}", query.id());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FavoriteSource> handle(GetFavoriteSourceByNewsApiKeyAndSourceIdQuery query) {
        log.debug("Querying favorite source by newsApiKey={}, sourceId={}", mask(query.newsApiKey().value()), query.sourceId().value());
        var result = favoriteSourceRepository.findByNewsApiKeyAndSourceId(query.newsApiKey(), query.sourceId());
        if (result.isEmpty()) log.debug("No favorite source found for newsApiKey={}, sourceId={}", mask(query.newsApiKey().value()), query.sourceId().value());
        return result;
    }

    /**
     * Returns a masked representation of a secret value, exposing only the last four characters.
     *
     * @param value the raw secret string to mask
     * @return masked string safe for log output
     */
    private static String mask(String value) {
        if (value == null || value.length() <= 4) return "****";
        return "****" + value.substring(value.length() - 4);
    }
}
