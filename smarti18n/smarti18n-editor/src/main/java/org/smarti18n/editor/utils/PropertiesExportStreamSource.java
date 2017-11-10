package org.smarti18n.editor.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;

import com.vaadin.server.StreamResource;
import org.smarti18n.api.Message;

public class PropertiesExportStreamSource implements StreamResource.StreamSource {

    private final Supplier<Collection<? extends Message>> supplier;
    private final Locale language;

    public PropertiesExportStreamSource(final Supplier<Collection<? extends Message>> supplier, final Locale language) {
        this.supplier = supplier;
        this.language = language;
    }

    @Override
    public InputStream getStream() {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            final Properties properties = new Properties();
            supplier.get()
                    .forEach(message -> {

                        final String key = message.getKey();
                        final String translation = message.getTranslation(language);

                        properties.setProperty(key, translation);
                    });

            properties.store(output, null);

            return new ByteArrayInputStream(output.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
