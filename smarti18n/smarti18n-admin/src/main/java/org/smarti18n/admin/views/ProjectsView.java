package org.smarti18n.admin.views;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;

/**
 * @author Marc Bellmann &lt;marc.bellmann@saxess.ag&gt;
 */
@UIScope
@SpringView(name = ProjectsView.VIEW_NAME)
public class ProjectsView extends AbstractView implements View {

    public static final String VIEW_NAME = "projects";

    private final ProjectsApi projectsApi;

    private Grid<Project> grid;

    public ProjectsView(final ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.admin.projects.caption"));

        setSizeFull();

        this.grid = new Grid<>(Project.class);
        this.grid.setSizeFull();
        this.grid.setColumns("displayName", "locales");

        addComponent(this.grid);
        setExpandRatio(this.grid, 1);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final List<Project> projects = this.projectsApi.findAll();

        grid.setItems(projects);
    }
}
