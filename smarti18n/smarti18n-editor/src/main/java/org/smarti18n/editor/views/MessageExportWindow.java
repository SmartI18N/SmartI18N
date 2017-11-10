package org.smarti18n.editor.views;

import java.util.Comparator;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.FormLayout;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.components.LanguageComboBox;
import org.smarti18n.editor.utils.PropertiesExportStreamSource;

class MessageExportWindow extends AbstractSmartI18nWindow {

    MessageExportWindow(final MessagesApi messagesApi, final String projectId) {
        super(I18N.getMessage("smarti18n.editor.message-export.caption"));

        final FormLayout formLayout = new FormLayout();

        final LanguageComboBox languageComboBox = new LanguageComboBox("Lang"
//                I18N.getMessage("smarti18n.editor.message-export.language")
        );
        formLayout.addComponent(languageComboBox);

        final StreamResource streamResource = new StreamResource(
                new PropertiesExportStreamSource(() -> messagesApi.findAll(projectId).stream()
                        .sorted(Comparator.comparing(Message::getKey))
                        .collect(Collectors.toList()),
                        languageComboBox.getValue()
                ),
                "smarti18n-" + projectId + ".properties"
        );
        final FileDownloader fileDownloader = new FileDownloader(
                streamResource
        );

        final IconButton downloadButton = new IconButton("Download", VaadinIcons.DOWNLOAD);
        downloadButton.addClickListener(event -> streamResource.setFilename(
                "smarti18n-" + projectId + "_" + languageComboBox.getValue().getLanguage() + ".properties"
        ));
        fileDownloader.extend(downloadButton);

        formLayout.addComponent(downloadButton);

        setContent(formLayout);
    }

}
