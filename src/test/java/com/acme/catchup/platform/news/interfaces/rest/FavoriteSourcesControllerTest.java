package com.acme.catchup.platform.news.interfaces.rest;

import com.acme.catchup.platform.news.application.commandservices.FavoriteSourceCommandService;
import com.acme.catchup.platform.news.application.queryservices.FavoriteSourceQueryService;
import com.acme.catchup.platform.news.domain.model.queries.GetFavoriteSourceByIdQuery;
import com.acme.catchup.platform.news.domain.model.queries.GetFavoriteSourceByNewsApiKeyAndSourceIdQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteSourcesControllerTest {
    @Mock
    private FavoriteSourceCommandService favoriteSourceCommandService;
    @Mock
    private FavoriteSourceQueryService favoriteSourceQueryService;
    @Mock
    private MessageSource messageSource;

    @Test
    void returnsNotFoundProblemDetailWhenFavoriteSourceByIdDoesNotExist() {
        when(favoriteSourceQueryService.handle(any(GetFavoriteSourceByIdQuery.class))).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("favorite.source.error.notFoundById"), any(), eq("favorite.source.error.notFoundById"), any(Locale.class)))
                .thenReturn("Favorite source not found for id 42.");

        var controller = new FavoriteSourcesController(favoriteSourceCommandService, favoriteSourceQueryService, messageSource);

        var response = controller.getFavoriteSourceById(42L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(ProblemDetail.class, response.getBody());
        var problemDetail = (ProblemDetail) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals("Favorite source not found for id 42.", problemDetail.getDetail());
    }

    @Test
    void returnsNotFoundProblemDetailWhenFavoriteSourceByCompositeKeyDoesNotExist() {
        when(favoriteSourceQueryService.handle(any(GetFavoriteSourceByNewsApiKeyAndSourceIdQuery.class))).thenReturn(Optional.empty());
        when(messageSource.getMessage(
                eq("favorite.source.error.notFoundByNewsApiKeyAndSourceId"),
                any(),
                eq("favorite.source.error.notFoundByNewsApiKeyAndSourceId"),
                any(Locale.class)))
                .thenReturn("Favorite source not found for the provided newsApiKey and sourceId.");

        var controller = new FavoriteSourcesController(favoriteSourceCommandService, favoriteSourceQueryService, messageSource);

        var response = controller.getFavoriteSourcesWithParameters("1", "1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(ProblemDetail.class, response.getBody());
        var problemDetail = (ProblemDetail) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals("Favorite source not found for the provided newsApiKey and sourceId.", problemDetail.getDetail());
    }
}

