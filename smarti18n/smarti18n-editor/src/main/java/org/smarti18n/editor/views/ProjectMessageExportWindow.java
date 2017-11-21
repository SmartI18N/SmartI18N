package org.smarti18n.editor.views;

import java.util.Comparator;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.FormLayout;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.components.LocaleComboBox;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.utils.PropertiesExportStreamSource;

class ProjectMessageExportWindow extends AbstractSmartI18nWindow {

    ProjectMessageExportWindow(final MessagesApi messagesApi, final Project project) {
        super(I18N.translate("smarti18n.editor.message-export.caption"));

        final String projectId = project.getId();

        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        final LocaleComboBox localeComboBox = new LocaleComboBox(
                I18N.translate("smarti18n.editor.message-export.locale"),
                project.getLocales()
        );
        formLayout.addComponent(localeComboBox);

        final StreamResource streamResource = new StreamResource(
                new PropertiesExportStreamSource(() -> messagesApi.findAll(projectId).stream()
                        .sorted(Comparator.comparing(Message::getKey))
                        .collect(Collectors.toList()),
                        localeComboBox.getValue()
                ),
                "smarti18n-" + projectId + ".properties"
        );
        final FileDownloader fileDownloader = new FileDownloader(
                streamResource
        );

        final IconButton downloadButton = new IconButton("Download", VaadinIcons.DOWNLOAD);
        downloadButton.addClickListener(event -> streamResource.setFilename(
                "smarti18n-" + projectId + "_" + localeComboBox.getValue().toString() + ".properties"
        ));
        fileDownloader.extend(downloadButton);

        formLayout.addComponent(downloadButton);

        setContent(formLayout);
    }

}
