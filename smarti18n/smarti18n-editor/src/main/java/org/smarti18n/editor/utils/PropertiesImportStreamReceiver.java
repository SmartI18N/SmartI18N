package org.smarti18n.editor.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

import com.vaadin.ui.Upload;

public class PropertiesImportStreamReceiver implements Upload.Receiver, Upload.SucceededListener {

    private final BiConsumer<Locale, ResourceBundle> consumer;
    private final ByteArrayOutputStream stream;

    public PropertiesImportStreamReceiver(final BiConsumer<Locale, ResourceBundle> consumer) {
        this.consumer = consumer;
        this.stream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream receiveUpload(final String filename, final String mimeType) {
        return stream;
    }

    @Override
    public void uploadSucceeded(final Upload.SucceededEvent event) {
        final String filename = event.getFilename();
        final String lang = filename.replace(".properties", "").split("_")[1];
        final Locale locale = new Locale(lang);

        try (final InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(stream.toByteArray()))) {
            final PropertyResourceBundle resourceBundle = new PropertyResourceBundle(reader);
            consumer.accept(locale, resourceBundle);
            stream.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
