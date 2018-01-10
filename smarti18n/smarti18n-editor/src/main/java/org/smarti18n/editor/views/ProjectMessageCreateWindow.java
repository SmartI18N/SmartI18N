package org.smarti18n.editor.views;

import com.vaadin.ui.TextField;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.vaadin.components.AddButton;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.utils.I18N;

import static org.smarti18n.vaadin.utils.VaadinUtils.navigateTo;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class ProjectMessageCreateWindow extends FormWindow {

    ProjectMessageCreateWindow(final EditorController editorController, final String projectId) {
        super(I18N.translate("smarti18n.editor.message-create.caption"));

        final TextField textFieldKey = new TextField(I18N.translate("smarti18n.editor.message-create.key"));
        addFormComponent(textFieldKey);

        final AddButton addButton = new AddButton(
                editorController.clickInsertMessage(projectId, textFieldKey, message -> {
                    navigateTo(ProjectMessageEditView.VIEW_NAME, projectId, message.getKey());
                    close();
                })
        );
        final CancelButton cancelButton = new CancelButton(clickEvent -> close());

        addFormButtons(addButton, cancelButton);
    }
}
