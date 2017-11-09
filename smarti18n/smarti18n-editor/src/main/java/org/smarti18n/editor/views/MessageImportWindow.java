package org.smarti18n.editor.views;

import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.I18N;

import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

class MessageImportWindow extends AbstractSmartI18nWindow {

    MessageImportWindow(final MessagesApi messagesApi, final String projectId) {
        super(I18N.getMessage("smarti18n.editor.message-import.caption"));

        final PropertiesImportStreamReceiver uploadReceiver = new PropertiesImportStreamReceiver((locale, resourceBundle) -> {
            resourceBundle.keySet().forEach(key -> {
                messagesApi.update(projectId, "default", key, resourceBundle.getString(key), locale);
            });
        });

        final Upload upload = new Upload("", uploadReceiver);
        upload.addSucceededListener(uploadReceiver);

        setContent(new VerticalLayout(
                upload
        ));
    }
}
