package org.smarti18n.vaadin.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;

import com.vaadin.server.StreamResource;
import org.smarti18n.models.Message;

public class PropertiesExportStreamSource implements StreamResource.StreamSource {

    private final Supplier<Collection<? extends Message>> supplier;
    private final Supplier<Locale> locale;

    public PropertiesExportStreamSource(final Supplier<Collection<? extends Message>> supplier, final Supplier<Locale> locale) {
        this.supplier = supplier;
        this.locale = locale;
    }

    @Override
    public InputStream getStream() {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            final Properties properties = new Properties();
            supplier.get()
                    .forEach(message -> {

                        final String key = message.getKey();
                        final String translation = message.getTranslation(locale.get());

                        properties.setProperty(key, translation);
                    });

            properties.store(output, "smarti18n Export");

            return new ByteArrayInputStream(output.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
