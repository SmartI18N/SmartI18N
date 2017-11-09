package org.smarti18n.editor.views;

import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.I18N;
import org.smarti18n.editor.vaadin.LanguageComboBox;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MessageImportWindow extends AbstractSmartI18nWindow {

    MessageImportWindow(final MessagesApi messagesApi, final String projectId) {
        super(I18N.getMessage("smarti18n.editor.message-import.caption"));

        final FormLayout formLayout = new FormLayout();

        final LanguageComboBox languageComboBox = new LanguageComboBox("Lang"
//                I18N.getMessage("smarti18n.editor.message-export.language")
        );
        formLayout.addComponent(languageComboBox);

        final Map<String, String> messages = new HashMap<>();
        final PropertiesImportStreamReceiver uploadReceiver = new PropertiesImportStreamReceiver((locale, resourceBundle) -> {
            resourceBundle.keySet().forEach(key -> messages.put(key, resourceBundle.getString(key)));
        });

        final Upload uploadButton = new Upload("", uploadReceiver);
        uploadButton.addSucceededListener(uploadReceiver);

        final Button importButton = new Button(
                I18N.getMessage("smarti18n.editor.message-import.caption"),
                event -> messages.forEach(
                        (key, value) -> messagesApi.update(projectId, "default", key, value, languageComboBox.getValue())
                )
        );

        formLayout.addComponent(new HorizontalLayout(uploadButton, importButton));

        setContent(formLayout);
    }
}
