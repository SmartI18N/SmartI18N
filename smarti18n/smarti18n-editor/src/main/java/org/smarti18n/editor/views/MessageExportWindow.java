package org.smarti18n.editor.views;

import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.I18N;
import org.smarti18n.editor.vaadin.IconButton;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

class MessageExportWindow extends AbstractSmartI18nWindow {

    MessageExportWindow(final MessagesApi messagesApi, final String projectId) {
        super(I18N.getMessage("smarti18n.editor.message-export.caption"));

        final FormLayout formLayout = new FormLayout();

        final ComboBox<Locale> languageComboBox = new ComboBox<>("Lang"
//                I18N.getMessage("smarti18n.editor.message-export.language")
        );
        languageComboBox.setItems(Locale.GERMAN, Locale.ENGLISH, Locale.ITALIAN);
        formLayout.addComponent(languageComboBox);

        final StreamResource streamResource = new StreamResource(
                new PropertiesExportStreamSource(() -> messagesApi.findAll(projectId, "default").stream()
                        .sorted(Comparator.comparing(Message::getKey))
                        .collect(Collectors.toList()),
                        languageComboBox.getValue()
                ),
                "smarti18n-" + projectId + ".properties"
        );
        final FileDownloader fileDownloader = new FileDownloader(
                streamResource
        );

        final Button downloadButton = new IconButton("Download", VaadinIcons.DOWNLOAD);
        downloadButton.addClickListener(event -> streamResource.setFilename(
                "smarti18n-" + projectId + "_" + languageComboBox.getValue().getLanguage() + ".properties"
        ));
        fileDownloader.extend(downloadButton);

        formLayout.addComponent(downloadButton);

        setContent(formLayout);
    }

}
