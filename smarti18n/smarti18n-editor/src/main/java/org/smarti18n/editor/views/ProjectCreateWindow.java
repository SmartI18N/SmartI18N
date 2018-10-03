package org.smarti18n.editor.views;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.Project;
import org.smarti18n.vaadin.components.AddButton;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.utils.I18N;

import static org.smarti18n.vaadin.utils.VaadinUtils.navigateTo;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectCreateWindow extends FormWindow {

    public ProjectCreateWindow(final EditorController editorController) {
        super(I18N.translate("smarti18n.editor.project-create.caption"));

        final TextField textFieldId = new TextField(I18N.translate("smarti18n.editor.project-create.id"));
        addFormComponent(textFieldId);

        final ComboBox<Project> parentProjectComboBox = new ComboBox<>(
                I18N.translate("smarti18n.editor.project-create.parent-project"),
                editorController.getProjects()
        );
        addFormComponent(parentProjectComboBox);

        final AddButton addButton = new AddButton(
                editorController.clickAddProject(textFieldId, parentProjectComboBox, () -> {
                    navigateTo(ProjectMessagesView.VIEW_NAME, textFieldId.getValue());
                    close();
                })
        );

        final CancelButton cancelButton = new CancelButton(
                clickEvent -> close()
        );

        addFormButtons(addButton, cancelButton);
    }

}
