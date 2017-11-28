package org.smarti18n.vaadin.utils;

import org.smarti18n.api.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class JsonExportStreamSource implements StreamResource.StreamSource {

    private final Supplier<Collection<? extends Message>> supplier;
    private final Supplier<Locale> locale;

    public JsonExportStreamSource(final Supplier<Collection<? extends Message>> supplier, final Supplier<Locale> locale) {
        this.supplier = supplier;
        this.locale = locale;
    }

    @Override
    public InputStream getStream() {
            final Map<String,String> map = new HashMap<>();
            supplier.get()
                    .forEach(message -> {
                        final String key = message.getKey();
                        final String translation = message.getTranslation(locale.get());

                        map.put(key, translation);
                    });

        try {
            final ObjectMapper objectMapper = new ObjectMapper();

            final byte[] bytes = objectMapper.writeValueAsBytes(map);

            return new ByteArrayInputStream(bytes);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
