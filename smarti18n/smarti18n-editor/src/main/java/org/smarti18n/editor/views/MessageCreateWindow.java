package org.smarti18n.editor.views;

import com.vaadin.server.Page;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.components.SaveButton;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class MessageCreateWindow extends AbstractSmartI18nWindow {

    MessageCreateWindow(final MessagesApi messagesApi, final String projectId) {
        super(I18N.getMessage("smarti18n.editor.message-create.caption"));

        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        final TextField textFieldKey = new TextField(I18N.getMessage("smarti18n.editor.message-create.key"));
        textFieldKey.setSizeFull();
        formLayout.addComponent(textFieldKey);

        final IconButton buttonSave = new SaveButton(clickEvent -> {
            messagesApi.insert(projectId, textFieldKey.getValue());
            close();
            Page.getCurrent().reload();
        });

        formLayout.addComponent(buttonSave);

        setContent(formLayout);
    }
}
