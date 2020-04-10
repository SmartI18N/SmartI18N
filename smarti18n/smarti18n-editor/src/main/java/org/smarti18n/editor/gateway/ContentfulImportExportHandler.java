package org.smarti18n.editor.gateway;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDALocale;
import com.contentful.java.cda.CDASpace;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.smarti18n.api2.MessagesApi;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Project;
import org.smarti18n.models.SingleMessageUpdateDTO;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.components.IconButton;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.vaadin.utils.VaadinExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ContentfulImportExportHandler implements ImportExportHandler {

    private final MessagesApi messagesApi;

    public ContentfulImportExportHandler(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @Override
    public String getName() {
        return "Contenful";
    }

    @Override
    public boolean hasImport() {
        return true;
    }

    @Override
    public boolean hasExport() {
        return false;
    }

    @Override
    public Window createImportWindow(final Project project) {
        final FormWindow formWindow = new FormWindow(I18N.translate("smarti18n.editor.message-import.caption"));

        final TextField spaceKeyField = new TextField(
                I18N.translate("smarti18n.editor.import-export.contentful.space-key")
        );
        formWindow.addFormComponent(spaceKeyField);

        final TextField accessTokenField = new TextField(
                I18N.translate("smarti18n.editor.import-export.contentful.access-token")
        );
        formWindow.addFormComponent(accessTokenField);

        final TextField contentTypeIdentifierField = new TextField(
                I18N.translate("smarti18n.editor.import-export.contentful.content-type-identifier")
        );
        formWindow.addFormComponent(contentTypeIdentifierField);

        final TextField keyIdentifierField = new TextField(
                I18N.translate("smarti18n.editor.import-export.contentful.key-identifier")
        );
        formWindow.addFormComponent(keyIdentifierField);

        final TextField valueIdentifierField = new TextField(
                I18N.translate("smarti18n.editor.import-export.contentful.value-identifier")
        );
        formWindow.addFormComponent(valueIdentifierField);


        final IconButton importButton = new IconButton(
                I18N.translate("smarti18n.editor.message-import.import", ""),
                VaadinIcons.UPLOAD,
                event -> {
                    final CDAClient cdaClient = CDAClient.builder()
                            .setSpace(spaceKeyField.getValue())
                            .setToken(accessTokenField.getValue())
                            .build();

                    final CDASpace cdaSpace = cdaClient.fetchSpace();
                    final CDAArray all = cdaClient
                            .fetch(CDAEntry.class)
                            .withContentType(contentTypeIdentifierField.getValue())
                            .all();
                    all.entries().values().forEach(cdaEntry -> {
                        for (final CDALocale cdaLocale : cdaSpace.locales()) {
                            final Locale locale = new Locale(cdaLocale.code());

                            cdaEntry.setLocale(cdaLocale.code());

                            final String key = cdaEntry.getField(keyIdentifierField.getValue());
                            final String value = cdaEntry.getField(valueIdentifierField.getValue());

                            try {
                                this.messagesApi.update(project.getId(), key, locale.toString(), new SingleMessageUpdateDTO(value));
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
                        }
                    });

                    formWindow.close();
                }
        );
        formWindow.addFormButtons(importButton);

        return formWindow;
    }

    @Override
    public Window createExportWindow(final Project project) {
        throw new UnsupportedOperationException("No Contentful Export available!");
    }
}
