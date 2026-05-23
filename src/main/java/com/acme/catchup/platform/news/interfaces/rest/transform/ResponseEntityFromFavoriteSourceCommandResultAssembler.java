package com.acme.catchup.platform.news.interfaces.rest.transform;

import com.acme.catchup.platform.news.application.commandservices.FavoriteSourceCommandFailure;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.shared.application.result.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Interface layer translator converting a favorite source command result into an HTTP ResponseEntity.
 * Crosses the boundary between the application-layer result ({@link Result}) and the HTTP response,
 * enabling the inbound service to translate command outcomes into client-facing representations.
 *
 * @since 1.0
 */
public class ResponseEntityFromFavoriteSourceCommandResultAssembler {
    /**
     * Converts a favorite source command result to a ResponseEntity.
     *
     * @param result result of the favorite source command execution
     * @param messageSource source for localized response messages
     * @return ResponseEntity with either the created favorite source resource or a conflict problem detail
     */
    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<FavoriteSource, FavoriteSourceCommandFailure> result,
            MessageSource messageSource) {
        return result.fold(
                source -> new ResponseEntity<>(FavoriteSourceResourceFromEntityAssembler.toResourceFromEntity(source), CREATED),
                failure -> {
                    var status = statusFromFailure(failure);
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(
                            status,
                            localizedMessage(messageSource, failure.messageKey())));
                });
    }

    private static HttpStatus statusFromFailure(FavoriteSourceCommandFailure failure) {
        if (failure instanceof FavoriteSourceCommandFailure.Duplicate) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private static String localizedMessage(MessageSource messageSource, String messageKey) {
        return messageSource.getMessage(messageKey, null, messageKey, LocaleContextHolder.getLocale());
    }
}


