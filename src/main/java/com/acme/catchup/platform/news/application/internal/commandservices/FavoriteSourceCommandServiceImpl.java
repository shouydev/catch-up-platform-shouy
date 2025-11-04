package com.acme.catchup.platform.news.application.internal.commandservices;

import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.domain.services.FavoriteSourceCommandService;
import com.acme.catchup.platform.news.infrastructure.persistence.jpa.FavoriteSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * FavoriteSourceCommandService Implementation
 *
 * @summary
 * Implementation of the FavoriteSourceCommandService interface.
 * It is responsible for handling favorite source commands.
 *
 * @since 1.0
 */
@Service
public class FavoriteSourceCommandServiceImpl implements FavoriteSourceCommandService {
    private final FavoriteSourceRepository favoriteSourceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteSourceCommandServiceImpl.class);
    private final MessageSource messageSource;

    public FavoriteSourceCommandServiceImpl(FavoriteSourceRepository favoriteSourceRepository, MessageSource messageSource) {
        this.favoriteSourceRepository = favoriteSourceRepository;
        this.messageSource = messageSource;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Optional<FavoriteSource> handle(CreateFavoriteSourceCommand command) {
        // In case the favorite source already exists, Log a localized error message and return empty.
        if (favoriteSourceRepository.existsByNewsApiKeyAndSourceId(command.newsApiKey(), command.sourceId())) {
            LOGGER.error(messageSource.getMessage("favorite.source.error.duplicate", null, LocaleContextHolder.getLocale()));
            return Optional.empty();
        }
        var favoriteSource = new FavoriteSource(command);
        var createdFavoriteSource = favoriteSourceRepository.save(favoriteSource);
        return Optional.of(createdFavoriteSource);
    }
}
