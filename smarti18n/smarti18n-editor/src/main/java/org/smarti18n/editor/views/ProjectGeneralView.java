package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.utils.I18N;

import javax.annotation.PostConstruct;

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

        final TextField textFieldId = new TextField(I18N.getMessage("smarti18n.editor.project-edit.id"));
        textFieldId.setReadOnly(true);
        textFieldId.setSizeFull();
        addComponent(textFieldId);

        final TextField textFieldName = new TextField(I18N.getMessage("smarti18n.editor.project-edit.name"));
        textFieldName.setSizeFull();
        addComponent(textFieldName);

        final TextArea textAreaDescription = new TextArea(I18N.getMessage("smarti18n.editor.project-edit.description"));
        textAreaDescription.setSizeFull();
        addComponent(textAreaDescription);

        final TextField textFieldSecret = new TextField(I18N.getMessage("smarti18n.editor.project-edit.secret"));
        textFieldSecret.setReadOnly(true);
        textFieldSecret.setSizeFull();
        addComponent(textFieldSecret);

        this.binder.forMemberField(textFieldId).bind("id");
        this.binder.forMemberField(textFieldName).bind("name");
        this.binder.forMemberField(textAreaDescription).bind("description");
        this.binder.forMemberField(textFieldSecret).bind("secret");
        this.binder.bindInstanceFields(this);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
    }

}
