package com.acme.catchup.platform.news.infrastructure.persistence.jpa.converters;

import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter for persisting {@link NewsApiKey} value objects as strings.
 */
@Converter
public class NewsApiKeyAttributeConverter implements AttributeConverter<NewsApiKey, String> {
    @Override
    public String convertToDatabaseColumn(NewsApiKey attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public NewsApiKey convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new NewsApiKey(dbData);
    }
}

