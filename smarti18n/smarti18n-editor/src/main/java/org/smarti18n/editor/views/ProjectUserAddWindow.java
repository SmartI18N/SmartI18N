package org.smarti18n.editor.views;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.AddButton;
import org.smarti18n.editor.components.CancelButton;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class ProjectUserAddWindow extends AbstractSmartI18nWindow {

    ProjectUserAddWindow(final ProjectsApi projectsApi, final String projectId) {
        super(I18N.translate("smarti18n.editor.user-add.caption"));

        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        final TextField mailTextField = new TextField(I18N.translate("smarti18n.editor.user-add.mail"));
        mailTextField.setSizeFull();
        formLayout.addComponent(mailTextField);

        final AddButton addButton = new AddButton(clickEvent -> {
            close();
        });
        final CancelButton cancelButton = new CancelButton(clickEvent -> close());

        formLayout.addComponent(new HorizontalLayout(addButton, cancelButton));

        setContent(formLayout);
    }
}
