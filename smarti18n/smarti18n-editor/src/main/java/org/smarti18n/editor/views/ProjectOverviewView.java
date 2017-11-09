package org.smarti18n.editor.views;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.IconButton;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectOverviewView.VIEW_NAME)
public class ProjectOverviewView extends AbstractView implements View {

    public static final String VIEW_NAME = "project/overview";

    private final ProjectsApi projectApi;

    private Grid<Project> grid;

    public ProjectOverviewView(final ProjectsApi projectApi) {
        this.projectApi = projectApi;
    }

    @PostConstruct
    void init() {
        setCaption(translate("smarti18n.editor.project-overview.caption"));

        addComponent(new Button(translate("smarti18n.editor.project-overview.add-new-project"), clickEvent -> {
            this.getUI().addWindow(new ProjectCreateWindow(this.projectApi));
        }));

        grid = new Grid<>(Project.class);

        grid.setColumns("displayName");
        grid.getColumn("displayName").setExpandRatio(1);

        grid.addComponentColumn(project -> new IconButton(VaadinIcons.MINUS, clickEvent -> {
            this.projectApi.remove(project.getId());
            Page.getCurrent().reload();
        }));

        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getId();
            navigator().navigateTo(ProjectEditView.VIEW_NAME + "/" + key);
        });

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);

        setSizeFull();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        grid.setItems(loadProjects());
    }

    private List<Project> loadProjects() {
        return this.projectApi.findAll().stream().collect(Collectors.toList());
    }
}
