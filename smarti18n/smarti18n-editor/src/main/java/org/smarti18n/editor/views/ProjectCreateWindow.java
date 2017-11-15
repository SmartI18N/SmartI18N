package org.smarti18n.editor.views;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.SaveButton;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.vaadin.VaadinUtils;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class ProjectCreateWindow extends AbstractSmartI18nWindow {

    ProjectCreateWindow(final ProjectsApi projectsApi) {
        super(I18N.getMessage("smarti18n.editor.project-create.caption"));

        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        final TextField textFieldId = new TextField(I18N.getMessage("smarti18n.editor.project-create.id"));
        textFieldId.setSizeFull();
        formLayout.addComponent(textFieldId);

        final SaveButton buttonSave = new SaveButton(clickEvent -> {
            final Project project = projectsApi.insert(textFieldId.getValue());
            close();
            getUI().getNavigator().navigateTo(
                    VaadinUtils.buildNavigation(ProjectEditView.VIEW_NAME, project.getId())
            );
        });

        formLayout.addComponent(buttonSave);

        setContent(formLayout);
    }
}
