package org.smarti18n.editor.gateway;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.editor.components.FormWindow;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.components.LocaleComboBox;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.utils.PropertiesExportStreamSource;
import org.smarti18n.editor.utils.PropertiesImportStreamReceiver;

@Component
public class SpringImportExportHandler implements ImportExportHandler {

    private final MessagesApi messagesApi;

    public SpringImportExportHandler(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @Override
    public String getName() {
        return "Spring Framework";
    }

    @Override
    public boolean hasImport() {
        return true;
    }

    @Override
    public boolean hasExport() {
        return true;
    }

    @Override
    public Window createImportWindow(final Project project) {
        final FormWindow formWindow = new FormWindow(I18N.translate("smarti18n.editor.message-import.caption"));

        final LocaleComboBox localeComboBox = new LocaleComboBox(
                I18N.translate("smarti18n.editor.message-export.locale"),
                project.getLocales()
        );
        formWindow.addFormComponent(localeComboBox);
        final Map<String, String> messages = new HashMap<>();
        final PropertiesImportStreamReceiver uploadReceiver = new PropertiesImportStreamReceiver((locale, resourceBundle) -> {
            resourceBundle.keySet().forEach(key -> messages.put(key, resourceBundle.getString(key)));
        });
        final Upload uploadButton = new Upload("", uploadReceiver);
        uploadButton.addSucceededListener(uploadReceiver);
        uploadButton.setVisible(true);
        final IconButton importButton = new IconButton(
                I18N.translate("smarti18n.editor.message-import.import", "0"),
                VaadinIcons.UPLOAD,
                event -> {
                    messages.forEach(
                            (key, value) -> messagesApi.update(project.getId(), key, localeComboBox.getValue(), value)
                    );
                    formWindow.close();
                }
        );
        importButton.setVisible(false);
        uploadButton.addSucceededListener(event -> {
            importButton.setCaption(
                    I18N.translate("smarti18n.editor.message-import.import", String.valueOf(messages.size()))
            );

            uploadButton.setVisible(false);
            importButton.setVisible(true);
        });
        formWindow.addFormButtons(uploadButton, importButton);

        return formWindow;
    }

    @Override
    public Window createExportWindow(final Project project) {
        final String projectId = project.getId();

        final FormWindow formWindow = new FormWindow(I18N.translate("smarti18n.editor.message-export.caption"));

        final LocaleComboBox localeComboBox = new LocaleComboBox(
                I18N.translate("smarti18n.editor.message-export.locale"),
                project.getLocales()
        );
        formWindow.addFormComponent(localeComboBox);

        final StreamResource streamResource = new StreamResource(
                new PropertiesExportStreamSource(() -> messagesApi.findAll(projectId).stream()
                        .sorted(Comparator.comparing(Message::getKey))
                        .collect(Collectors.toList()),
                        localeComboBox::getValue
                ),
                "smarti18n-" + projectId + ".properties"
        );

        final FileDownloader fileDownloader = new FileDownloader(
                streamResource
        );

        final IconButton downloadButton = new IconButton("Download", VaadinIcons.DOWNLOAD);
        downloadButton.addClickListener(event -> {
            streamResource.setFilename(
                    "smarti18n-" + projectId + "_" + localeComboBox.getValue().toString() + ".properties"
            );
            formWindow.close();
        });

        fileDownloader.extend(downloadButton);
        formWindow.addFormButtons(downloadButton);

        return formWindow;
    }

}
