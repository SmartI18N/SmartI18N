package org.smarti18n.editor.views;

import java.util.Optional;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectEditView.VIEW_NAME)
public class ProjectEditView extends AbstractView implements View {

    static final String VIEW_NAME = "project/edit";

    private final ProjectsApi projectsApi;

    private TextField textFieldId;
    private TextField textFieldName;
    private TextArea textAreaDescription;


    protected ProjectEditView(final I18N i18N, final ProjectsApi projectsApi) {
        super(i18N);
        this.projectsApi = projectsApi;
    }

    @PostConstruct
    private void init() {
        setCaption(translate("smarti18n.editor.project-edit.caption"));
        setSizeFull();

        final Layout layout = new FormLayout();
        layout.setSizeFull();

        this.textFieldId = new TextField(translate("smarti18n.editor.project-edit.id"));
        this.textFieldId.setReadOnly(true);
        this.textFieldId.setSizeFull();
        layout.addComponent(this.textFieldId);

        this.textFieldName = new TextField(translate("smarti18n.editor.project-edit.name"));
        this.textFieldName.setSizeFull();
        layout.addComponent(this.textFieldName);

        this.textAreaDescription = new TextArea(translate("smarti18n.editor.project-edit.description"));
        this.textAreaDescription.setSizeFull();
        layout.addComponent(this.textAreaDescription);

        addComponent(layout);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String projectId = viewChangeEvent.getParameters();

        // TODO
        final Optional<? extends Project> first = this.projectsApi.findAll().stream()
                .filter(project -> project.getId().equals(projectId)).findFirst();

        final ProjectImpl bean = new ProjectImpl(first.get());

        this.textFieldId.setValue(bean.getId());
        this.textFieldName.setValue(bean.getName());
        this.textAreaDescription.setValue(bean.getDescription());
    }
}
