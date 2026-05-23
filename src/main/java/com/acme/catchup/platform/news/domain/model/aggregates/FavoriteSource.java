package com.acme.catchup.platform.news.domain.model.aggregates;

import com.acme.catchup.platform.news.domain.model.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;
import com.acme.catchup.platform.news.infrastructure.persistence.jpa.converters.NewsApiKeyAttributeConverter;
import com.acme.catchup.platform.news.infrastructure.persistence.jpa.converters.SourceIdAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Aggregate root enforcing the domain invariant: each news API key can have
 * at most one favorite marking per news source.
 *
 * A FavoriteSource represents a user's explicit interest in tracking a specific
 * news source within their news API context. The aggregate maintains these
 * invariants:
 * - Composite key (newsApiKey, sourceId) must be unique; only one FavoriteSource
 *   per user-source pair can exist
 * - Both newsApiKey and sourceId are required (non-null, non-blank)
 *
 * Created by {@link CreateFavoriteSourceCommand}.
 *
 * @since 1.0
 */
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"news_api_key", "source_id"}, name = FavoriteSource.NEWS_API_KEY_SOURCE_ID_UNIQUE_CONSTRAINT)
})
public class FavoriteSource extends AbstractAggregateRoot<FavoriteSource> {

    /** Unique constraint name shared with the persistence layer. */
    public static final String NEWS_API_KEY_SOURCE_ID_UNIQUE_CONSTRAINT = "uk_favorite_source_news_api_key_source_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = NewsApiKeyAttributeConverter.class)
    private NewsApiKey newsApiKey;

    @Column(nullable = false)
    @Convert(converter = SourceIdAttributeConverter.class)
    private SourceId sourceId;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    protected FavoriteSource() {}

    /**
     * Creates a favorite source aggregate from a create command.
     *
     * @param command command containing the news API key and source ID
     */
    public FavoriteSource(CreateFavoriteSourceCommand command) {
        this.newsApiKey = command.newsApiKey();
        this.sourceId = command.sourceId();
    }


}
