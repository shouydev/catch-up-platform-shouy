package com.acme.catchup.platform.news.infrastructure.persistence.jpa.converters;

import com.acme.catchup.platform.news.domain.model.valueobjects.SourceId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SourceIdAttributeConverterTest {
    private final SourceIdAttributeConverter converter = new SourceIdAttributeConverter();

    @Test
    void convertsValueObjectToDatabaseColumn() {
        assertEquals("source-id", converter.convertToDatabaseColumn(new SourceId("source-id")));
    }

    @Test
    void convertsDatabaseColumnToValueObject() {
        assertEquals("source-id", converter.convertToEntityAttribute("source-id").value());
    }

    @Test
    void handlesNullValues() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }
}

