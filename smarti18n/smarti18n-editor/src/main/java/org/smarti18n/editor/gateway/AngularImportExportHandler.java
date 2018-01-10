package org.smarti18n.editor.gateway;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Window;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;
import org.smarti18n.models.Project;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.components.IconButton;
import org.smarti18n.vaadin.components.LocaleComboBox;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.vaadin.utils.JsonExportStreamSource;
import org.smarti18n.vaadin.utils.VaadinExceptionHandler;

@Component
public class AngularImportExportHandler implements ImportExportHandler {

    private final MessagesApi messagesApi;

    public AngularImportExportHandler(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @Override
    public String getName() {
        return "AngularJS";
    }

    @Override
    public boolean hasImport() {
        return false;
    }

    @Override
    public boolean hasExport() {
        return true;
    }

    @Override
    public Window createImportWindow(final Project project) {
        return null;
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
                new JsonExportStreamSource(
                        findMessages(projectId),
                        localeComboBox::getValue
                ),
                "smarti18n-" + projectId + ".json"
        );

        final FileDownloader fileDownloader = new FileDownloader(
                streamResource
        );

        final IconButton downloadButton = new IconButton("Download", VaadinIcons.DOWNLOAD);
        downloadButton.addClickListener(event -> {
            streamResource.setFilename(
                    "smarti18n-" + projectId + "_" + localeComboBox.getValue().toString() + ".json"
            );
            formWindow.close();
        });

        fileDownloader.extend(downloadButton);
        formWindow.addFormButtons(downloadButton);

        return formWindow;
    }

    private Supplier<Collection<? extends Message>> findMessages(final String projectId) {
        return () -> {
            try {
                return messagesApi.findAll(projectId).stream()
                        .sorted(Comparator.comparing(Message::getKey))
                        .collect(Collectors.toList());

            } catch (ProjectUnknownException e) {
                VaadinExceptionHandler.handleProjectUnknownException();
                throw new IllegalStateException(e);
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
                throw new IllegalStateException(e);
            } catch (UserRightsException e) {
                VaadinExceptionHandler.handleUserRightsException();
                throw new IllegalStateException(e);
            }
        };
    }
}
