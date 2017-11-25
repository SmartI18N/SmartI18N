package org.smarti18n.editor.views;

import com.vaadin.ui.TextField;

import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.vaadin.components.AddButton;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.vaadin.utils.VaadinUtils;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class ProjectMessageCreateWindow extends FormWindow {

    ProjectMessageCreateWindow(final MessagesApi messagesApi, final String projectId) {
        super(I18N.translate("smarti18n.editor.message-create.caption"));

        final TextField textFieldKey = new TextField(I18N.translate("smarti18n.editor.message-create.key"));
        addFormComponent(textFieldKey);

        final AddButton addButton = new AddButton(clickEvent -> {
            final Message message = messagesApi.insert(projectId, textFieldKey.getValue());

            getUI().getNavigator().navigateTo(
                    VaadinUtils.buildNavigation(ProjectMessageEditView.VIEW_NAME, projectId, message.getKey())
            );
            close();
        });
        final CancelButton cancelButton = new CancelButton(clickEvent -> close());

        addFormButtons(addButton, cancelButton);
    }
}
