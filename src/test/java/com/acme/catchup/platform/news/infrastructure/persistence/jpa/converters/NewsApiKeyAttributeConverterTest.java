package com.acme.catchup.platform.news.infrastructure.persistence.jpa.converters;

import com.acme.catchup.platform.news.domain.model.valueobjects.NewsApiKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NewsApiKeyAttributeConverterTest {
    private final NewsApiKeyAttributeConverter converter = new NewsApiKeyAttributeConverter();

    @Test
    void convertsValueObjectToDatabaseColumn() {
        assertEquals("news-api-key", converter.convertToDatabaseColumn(new NewsApiKey("news-api-key")));
    }

    @Test
    void convertsDatabaseColumnToValueObject() {
        assertEquals("news-api-key", converter.convertToEntityAttribute("news-api-key").value());
    }

    @Test
    void handlesNullValues() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }
}

