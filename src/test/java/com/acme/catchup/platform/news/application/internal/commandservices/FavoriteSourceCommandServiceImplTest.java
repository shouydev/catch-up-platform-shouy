package com.acme.catchup.platform.news.application.internal.commandservices;

import com.acme.catchup.platform.news.application.commandservices.FavoriteSourceCommandFailure;
import com.acme.catchup.platform.news.domain.model.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;
import com.acme.catchup.platform.news.infrastructure.persistence.jpa.FavoriteSourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteSourceCommandServiceImplTest {
    @Mock
    private FavoriteSourceRepository favoriteSourceRepository;

    @Test
    void returnsFailureWhenFavoriteSourceAlreadyExists() {
        var service = new FavoriteSourceCommandServiceImpl(favoriteSourceRepository);
        var newsApiKey = new NewsApiKey("news-api-key");
        var sourceId = new SourceId("source-id");
        var command = new CreateFavoriteSourceCommand(newsApiKey, sourceId);
        when(favoriteSourceRepository.existsByNewsApiKeyAndSourceId(eq(newsApiKey), eq(sourceId))).thenReturn(true);

        var result = service.handle(command);

        assertTrue(result.isFailure());
        assertInstanceOf(FavoriteSourceCommandFailure.Duplicate.class, result.failure().orElseThrow());
        verify(favoriteSourceRepository).existsByNewsApiKeyAndSourceId(eq(newsApiKey), eq(sourceId));
        verify(favoriteSourceRepository, never()).save(any(FavoriteSource.class));
    }

    @Test
    void returnsSuccessWhenFavoriteSourceIsCreated() {
        var service = new FavoriteSourceCommandServiceImpl(favoriteSourceRepository);
        var newsApiKey = new NewsApiKey("news-api-key");
        var sourceId = new SourceId("source-id");
        var command = new CreateFavoriteSourceCommand(newsApiKey, sourceId);
        var createdFavoriteSource = new FavoriteSource(command);
        when(favoriteSourceRepository.existsByNewsApiKeyAndSourceId(eq(newsApiKey), eq(sourceId))).thenReturn(false);
        when(favoriteSourceRepository.save(any(FavoriteSource.class))).thenReturn(createdFavoriteSource);

        var result = service.handle(command);

        assertTrue(result.isSuccess());
        assertEquals(createdFavoriteSource, result.success().orElseThrow());
        verify(favoriteSourceRepository).existsByNewsApiKeyAndSourceId(eq(newsApiKey), eq(sourceId));
        verify(favoriteSourceRepository).save(any(FavoriteSource.class));
    }
}


