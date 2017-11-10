package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.vaadin.AbstractView;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectEditView.VIEW_NAME)
public class ProjectEditView extends AbstractView implements View {

    static final String VIEW_NAME = "project/edit";

    private final ProjectsApi projectsApi;

    private final Binder<Project> binder;

    protected ProjectEditView(final ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;

        this.binder = new Binder<>(Project.class);
    }

    @PostConstruct
    private void init() {
        setCaption(translate("smarti18n.editor.project-edit.caption"));
        setSizeFull();

        addComponent(createButtonBar());

        final Layout layout = new FormLayout();
        layout.setSizeFull();

        final TextField textFieldId = new TextField(translate("smarti18n.editor.project-edit.id"));
        textFieldId.setReadOnly(true);
        textFieldId.setSizeFull();
        layout.addComponent(textFieldId);

        final TextField textFieldName = new TextField(translate("smarti18n.editor.project-edit.name"));
        textFieldName.setSizeFull();
        layout.addComponent(textFieldName);

        final TextArea textAreaDescription = new TextArea(translate("smarti18n.editor.project-edit.description"));
        textAreaDescription.setSizeFull();
        layout.addComponent(textAreaDescription);

        this.binder.forMemberField(textFieldId).bind("id");
        this.binder.forMemberField(textFieldName).bind("name");
        this.binder.forMemberField(textAreaDescription).bind("description");
        this.binder.bindInstanceFields(this);

        addComponent(layout);
        setExpandRatio(layout, 1);
    }

    private HorizontalLayout createButtonBar() {

        final Button buttonSave = new Button(translate("common.save"));
        buttonSave.addClickListener(clickEvent -> {
            final Project project = new ProjectImpl();
            binder.writeBeanIfValid(project);
            projectsApi.update(project);

            navigator().navigateTo(ProjectOverviewView.VIEW_NAME);
        });

        final Button buttonCancle = new Button(translate("common.cancle"));
        buttonCancle.addClickListener(clickEvent -> navigator().navigateTo(ProjectOverviewView.VIEW_NAME));

        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        buttonLayout.addComponents(buttonSave, buttonCancle);

        return buttonLayout;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String projectId = viewChangeEvent.getParameters();

        final Project project = this.projectsApi.findOne(projectId);

        this.binder.readBean(project);
    }
}
