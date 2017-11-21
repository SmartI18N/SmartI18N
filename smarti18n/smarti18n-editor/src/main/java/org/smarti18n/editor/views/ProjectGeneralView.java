package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectGeneralView.VIEW_NAME)
class ProjectGeneralView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/general";

    private final Binder<Project> binder;

    ProjectGeneralView(final ProjectsApi projectsApi) {
        super(projectsApi);

        this.binder = new Binder<>(Project.class);
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.project-edit.caption"));

        setSizeFull();
        setMargin(true);

        final TextField textFieldId = new TextField(I18N.translate("smarti18n.editor.project-edit.id"));
        textFieldId.setReadOnly(true);
        textFieldId.setSizeFull();

        final TextField textFieldName = new TextField(I18N.translate("smarti18n.editor.project-edit.name"));
        textFieldName.setSizeFull();

        final TextArea textAreaDescription = new TextArea(I18N.translate("smarti18n.editor.project-edit.description"));
        textAreaDescription.setSizeFull();

        final TextField textFieldSecret = new TextField(I18N.translate("smarti18n.editor.project-edit.secret"));
        textFieldSecret.setReadOnly(true);
        textFieldSecret.setSizeFull();

        final FormLayout form = new FormLayout(textFieldId, textFieldName, textAreaDescription, textFieldSecret);
        form.setMargin(true);

        final Panel panel = new Panel(
                form
        );
        addComponent(panel);
        setExpandRatio(panel, 1);

        this.binder.forMemberField(textFieldId).bind("id");
        this.binder.forMemberField(textFieldName).bind("name");
        this.binder.forMemberField(textAreaDescription).bind("description");
        this.binder.forMemberField(textFieldSecret).bind("secret");
        this.binder.bindInstanceFields(this);
    }

    @Override
    protected HorizontalLayout createButtonBar() {
        final IconButton saveButton = new IconButton(I18N.translate("common.save"), VaadinIcons.SAFE, clickEvent -> {
            try {
                final Project project = projectContext.getProject();
                binder.writeBean(project);

                projectsApi.update(project);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });

        final IconButton removeProjectButton = new IconButton(I18N.translate("smarti18n.editor.project-general.remove-project.button"), VaadinIcons.FOLDER_REMOVE, clickEvent -> {
                projectsApi.remove(projectContext.getProjectId());
                navigateTo(StartView.VIEW_NAME);
        });
        removeProjectButton.addStyleName(ValoTheme.BUTTON_DANGER);

        return new HorizontalLayout(
                saveButton,
                removeProjectButton
        );
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);

        final Project project = projectContext.getProject();
        this.binder.readBean(project);
    }

}
