package org.smarti18n.editor.views;

import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.I18N;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Upload;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

@UIScope
@SpringView(name = MessageImportView.VIEW_NAME)
public class MessageImportView extends AbstractView implements View {

    public static final String VIEW_NAME = "message/import";

    private final MessagesApi messagesApi;

    public MessageImportView(final I18N i18N, final MessagesApi messagesApi) {
        super(i18N);
        this.messagesApi = messagesApi;
    }

    @PostConstruct
    private void init() {
        setCaption(translate("smarti18n.editor.message-import.caption"));

        final PropertiesUploadReceiver uploadReceiver = new PropertiesUploadReceiver((locale, resourceBundle) -> {
            resourceBundle.keySet()
                    .forEach(key -> {
                        messagesApi.save(key, resourceBundle.getString(key), locale);
                    });
        });



        final Upload upload = new Upload("Upload it here", uploadReceiver);
        upload.addSucceededListener(uploadReceiver);

        addComponent(upload);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    private static class PropertiesUploadReceiver implements Upload.Receiver, Upload.SucceededListener {

        private final BiConsumer<Locale, ResourceBundle> consumer;
        private final ByteArrayOutputStream stream;

        PropertiesUploadReceiver(final BiConsumer<Locale, ResourceBundle> consumer) {
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
}
