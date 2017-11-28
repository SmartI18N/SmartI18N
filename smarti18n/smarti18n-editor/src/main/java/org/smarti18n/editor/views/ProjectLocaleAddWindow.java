package org.smarti18n.editor.views;

import java.util.Arrays;
import java.util.Locale;

import com.vaadin.ui.ComboBox;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.vaadin.components.AddButton;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class ProjectLocaleAddWindow extends FormWindow {

    ProjectLocaleAddWindow(final ProjectsApi projectsApi, final String projectId) {
        super(I18N.translate("smarti18n.editor.locale-add.caption"));

        final ComboBox<Locale> localeComboBox = new ComboBox<>(
                I18N.translate("smarti18n.editor.locale-add.locale"),
                Arrays.asList(Locale.getAvailableLocales())
        );
        addFormComponent(localeComboBox);

        final AddButton addButton = new AddButton(clickEvent -> {
            final Project project = projectsApi.findOne(projectId);
            project.getLocales().add(localeComboBox.getValue());
            projectsApi.update(project);

            close();
        });
        final CancelButton cancelButton = new CancelButton(clickEvent -> close());

        addFormButtons(addButton, cancelButton);
    }
}
