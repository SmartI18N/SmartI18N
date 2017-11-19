package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.LabelSet;
import org.smarti18n.editor.utils.I18N;

import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectUsersView.VIEW_NAME)
public class ProjectUsersView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/general";

    private final Binder<Project> binder;

    ProjectUsersView(final ProjectsApi projectsApi) {
        super(projectsApi);

        this.binder = new Binder<>(Project.class);
    }

    @PostConstruct
    public void init() {
        super.init(I18N.getMessage("smarti18n.editor.project-edit.owners"));

        final LabelSet labelSet = new LabelSet("");
        labelSet.setSizeFull();
        addComponent(labelSet);

        this.binder.forMemberField(labelSet).bind("owners");
        this.binder.bindInstanceFields(this);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
    }
}
