package org.smarti18n.editor.views;

import com.vaadin.ui.TextField;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.AddButton;
import org.smarti18n.editor.components.CancelButton;
import org.smarti18n.editor.components.FormWindow;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.vaadin.VaadinUtils;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectCreateWindow extends FormWindow {

    public ProjectCreateWindow(final ProjectsApi projectsApi) {
        super(I18N.translate("smarti18n.editor.project-create.caption"));

        final TextField textFieldId = new TextField(I18N.translate("smarti18n.editor.project-create.id"));
        addFormComponent(textFieldId);

        final AddButton addButton = new AddButton(clickEvent -> {
            final Project project = projectsApi.insert(textFieldId.getValue());

            getUI().getNavigator().navigateTo(
                    VaadinUtils.buildNavigation(ProjectMessagesView.VIEW_NAME, project.getId())
            );
            close();
        });
        final CancelButton cancelButton = new CancelButton(clickEvent -> close());

        addFormButtons(addButton, cancelButton);
    }
}
