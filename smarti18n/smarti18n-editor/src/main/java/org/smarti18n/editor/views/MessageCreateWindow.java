package org.smarti18n.editor.views;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.I18N;

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

        final Button buttonSave = new Button(
                I18N.getMessage("common.save"),
                clickEvent -> {
                    messagesApi.insert(projectId, textFieldKey.getValue());
                    close();
                    Page.getCurrent().reload();
                }
        );

        formLayout.addComponent(buttonSave);

        setContent(formLayout);
    }
}
