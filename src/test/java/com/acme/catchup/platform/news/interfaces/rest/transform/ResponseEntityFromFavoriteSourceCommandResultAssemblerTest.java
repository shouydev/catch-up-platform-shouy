package com.acme.catchup.platform.news.interfaces.rest.transform;

import com.acme.catchup.platform.news.application.commandservices.FavoriteSourceCommandFailure;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;
import com.acme.catchup.platform.news.interfaces.rest.resources.FavoriteSourceResource;
import com.acme.catchup.platform.shared.application.result.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseEntityFromFavoriteSourceCommandResultAssemblerTest {
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUpLocale() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @AfterEach
    void clearLocale() {
        LocaleContextHolder.resetLocaleContext();
    }

    @Test
    void convertsSuccessResultToCreatedResponseEntity() {
        var command = new CreateFavoriteSourceCommand(new NewsApiKey("news-api-key"), new SourceId("source-id"));
        var favoriteSource = new FavoriteSource(command);

        ResponseEntity<?> response = ResponseEntityFromFavoriteSourceCommandResultAssembler.toResponseEntityFromResult(
                Result.success(favoriteSource), messageSource);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertInstanceOf(FavoriteSourceResource.class, response.getBody());
        var resource = (FavoriteSourceResource) response.getBody();
        assertEquals("news-api-key", resource.newsApiKey());
        assertEquals("source-id", resource.sourceId());
        assertNull(resource.id());
    }

    @Test
    void convertsDuplicateFailureToConflictProblemDetailResponseEntity() {
        when(messageSource.getMessage(eq("favorite.source.error.duplicate"), any(), eq("favorite.source.error.duplicate"), eq(Locale.ENGLISH)))
                .thenReturn("Favorite source already exists");

        ResponseEntity<?> response = ResponseEntityFromFavoriteSourceCommandResultAssembler.toResponseEntityFromResult(
                Result.failure(new FavoriteSourceCommandFailure.Duplicate()), messageSource);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertInstanceOf(ProblemDetail.class, response.getBody());
        var problemDetail = (ProblemDetail) response.getBody();
        assertEquals(HttpStatus.CONFLICT.value(), problemDetail.getStatus());
        assertEquals("Favorite source already exists", problemDetail.getDetail());
    }
}


