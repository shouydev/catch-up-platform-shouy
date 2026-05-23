package com.acme.catchup.platform.news.application.internal.commandservices;

import com.acme.catchup.platform.news.application.commandservices.FavoriteSourceCommandFailure;
import com.acme.catchup.platform.news.application.commandservices.FavoriteSourceCommandService;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.infrastructure.persistence.jpa.FavoriteSourceRepository;
import static com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource.NEWS_API_KEY_SOURCE_ID_UNIQUE_CONSTRAINT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acme.catchup.platform.shared.application.result.Result;

/**
 * Application service for favorite source command operations.
 * Contains the command handler method that orchestrates the domain invariant
 * ensuring (newsApiKey, sourceId) must be unique.
 * Handles duplicate key constraint violations by returning an application-layer failure,
 * implementing idempotency at the application layer rather than raising exceptions.
 *
 * @since 1.0
 */
@Slf4j
@Service
public class FavoriteSourceCommandServiceImpl implements FavoriteSourceCommandService {
    private static final String DUPLICATE_FAVORITE_SOURCE_CONSTRAINT = NEWS_API_KEY_SOURCE_ID_UNIQUE_CONSTRAINT;
    private final FavoriteSourceRepository favoriteSourceRepository;

    public FavoriteSourceCommandServiceImpl(FavoriteSourceRepository favoriteSourceRepository) {
        this.favoriteSourceRepository = favoriteSourceRepository;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    @Transactional
    public Result<FavoriteSource, FavoriteSourceCommandFailure> handle(CreateFavoriteSourceCommand command) {
        if (favoriteSourceRepository.existsByNewsApiKeyAndSourceId(command.newsApiKey(), command.sourceId())) {
            log.warn("Duplicate favorite source detected (pre-check): newsApiKey={}, sourceId={}",
                    mask(command.newsApiKey().value()), command.sourceId().value());
            return duplicateResult();
        }
        try {
            var favoriteSource = new FavoriteSource(command);
            var createdFavoriteSource = favoriteSourceRepository.save(favoriteSource);
            log.info("Favorite source created: newsApiKey={}, sourceId={}, id={}, createdAt={}, updatedAt={}",
                    mask(command.newsApiKey().value()),
                    command.sourceId().value(),
                    createdFavoriteSource.getId(),
                    createdFavoriteSource.getCreatedAt(),
                    createdFavoriteSource.getUpdatedAt());
            return Result.success(createdFavoriteSource);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicateFavoriteSourceViolation(exception)) {
                log.warn("Duplicate favorite source detected (constraint violation): newsApiKey={}, sourceId={}",
                        mask(command.newsApiKey().value()), command.sourceId().value());
                return duplicateResult();
            }
            log.error("Unexpected data integrity violation saving favorite source: newsApiKey={}, sourceId={}",
                    mask(command.newsApiKey().value()), command.sourceId().value(), exception);
            throw exception;
        }
    }

    private Result<FavoriteSource, FavoriteSourceCommandFailure> duplicateResult() {
        return Result.failure(new FavoriteSourceCommandFailure.Duplicate());
    }

    /**
     * Returns a masked representation of a secret value, exposing only the last four characters.
     * For example {@code "abc123xyz"} becomes {@code "***xyz"}.
     *
     * @param value the raw secret string to mask
     * @return masked string safe for log output
     */
    private static String mask(String value) {
        if (value == null || value.length() <= 4) return "****";
        return "****" + value.substring(value.length() - 4);
    }

    /**
     * Detects whether a data integrity violation corresponds to the favorite source
     * unique constraint for {@code newsApiKey + sourceId}.
     *
     * @param exception persistence exception raised during save
     * @return {@code true} when the exception chain contains the duplicate constraint name
     */
    private boolean isDuplicateFavoriteSourceViolation(DataIntegrityViolationException exception) {
        Throwable violationCause = exception;
        while (violationCause != null) {
            String message = violationCause.getMessage();
            if (message != null && message.contains(DUPLICATE_FAVORITE_SOURCE_CONSTRAINT)) return true;
            violationCause = violationCause.getCause();
        }
        return false;
    }
}
