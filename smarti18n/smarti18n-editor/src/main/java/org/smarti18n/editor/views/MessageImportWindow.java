package org.smarti18n.editor.views;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.components.LanguageComboBox;
import org.smarti18n.editor.utils.PropertiesImportStreamReceiver;

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

        final IconButton importButton = new IconButton(
                I18N.getMessage("smarti18n.editor.message-import.import"),
                VaadinIcons.UPLOAD,
                event -> messages.forEach(
                        (key, value) -> messagesApi.update(projectId, key, value, languageComboBox.getValue())
                )
        );

        formLayout.addComponent(new HorizontalLayout(uploadButton, importButton));

        setContent(formLayout);
    }
}
