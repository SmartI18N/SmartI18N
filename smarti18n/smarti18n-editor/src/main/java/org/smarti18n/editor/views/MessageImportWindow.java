package org.smarti18n.editor.views;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.components.LocaleComboBox;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.utils.PropertiesImportStreamReceiver;

class MessageImportWindow extends AbstractSmartI18nWindow {

    MessageImportWindow(final MessagesApi messagesApi, final Project project) {
        super(I18N.getMessage("smarti18n.editor.message-import.caption"));

        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        final LocaleComboBox localeComboBox = new LocaleComboBox(
                I18N.getMessage("smarti18n.editor.message-export.locale"),
                project.getLocales()
        );
        formLayout.addComponent(localeComboBox);

        final Map<String, String> messages = new HashMap<>();
        final PropertiesImportStreamReceiver uploadReceiver = new PropertiesImportStreamReceiver((locale, resourceBundle) -> {
            resourceBundle.keySet().forEach(key -> messages.put(key, resourceBundle.getString(key)));
        });

        final Upload uploadButton = new Upload("", uploadReceiver);
        uploadButton.addSucceededListener(uploadReceiver);
        uploadButton.setVisible(true);

        final IconButton importButton = new IconButton(
                I18N.getMessage("smarti18n.editor.message-import.import", "0"),
                VaadinIcons.UPLOAD,
                event -> messages.forEach(
                        (key, value) -> messagesApi.update(project.getId(), key, localeComboBox.getValue(), value)
                )
        );
        importButton.setVisible(false);

        uploadButton.addSucceededListener(event -> {
            importButton.setCaption(
                    I18N.getMessage("smarti18n.editor.message-import.import", String.valueOf(messages.size()))
            );

            uploadButton.setVisible(false);
            importButton.setVisible(true);
        });

        formLayout.addComponent(new HorizontalLayout(uploadButton, importButton));

        setContent(formLayout);
    }
}
