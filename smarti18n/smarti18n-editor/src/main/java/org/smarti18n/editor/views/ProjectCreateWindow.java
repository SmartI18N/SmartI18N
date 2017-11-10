package org.smarti18n.editor.views;

import com.vaadin.server.Page;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.vaadin.I18N;
import org.smarti18n.editor.vaadin.SaveButton;

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
            projectsApi.insert(textFieldId.getValue());
            close();
            Page.getCurrent().reload();
        });

        formLayout.addComponent(buttonSave);

        setContent(formLayout);
    }
}
