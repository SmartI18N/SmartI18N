package org.smarti18n.editor.views;

import com.vaadin.ui.TextField;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.AddButton;
import org.smarti18n.editor.components.CancelButton;
import org.smarti18n.editor.components.FormWindow;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class ProjectUserAddWindow extends FormWindow {

    ProjectUserAddWindow(final ProjectsApi projectsApi, final String projectId) {
        super(I18N.translate("smarti18n.editor.user-add.caption"));

        final TextField mailTextField = new TextField(I18N.translate("smarti18n.editor.user-add.mail"));
        addFormComponent(mailTextField);

        final AddButton addButton = new AddButton(clickEvent -> {
            close();
        });
        final CancelButton cancelButton = new CancelButton(clickEvent -> close());

        addFormButtons(addButton, cancelButton);
    }
}
