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
import com.vaadin.ui.Grid;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.AbstractView;
import org.smarti18n.editor.components.IconButton;

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

        final IconButton iconButton = new IconButton(
                translate("smarti18n.editor.project-overview.add-new-project"),
                VaadinIcons.FILE_ADD,
                clickEvent -> this.getUI().addWindow(new ProjectCreateWindow(this.projectApi))
        );
        addComponent(iconButton);

        grid = new Grid<>(Project.class);

        grid.setColumns("displayName");
        grid.getColumn("displayName").setExpandRatio(1);

        grid.addComponentColumn(project -> new IconButton(VaadinIcons.MINUS, clickEvent -> {
            this.projectApi.remove(project.getId());
            Page.getCurrent().reload();
        }));

        grid.addItemClickListener(itemClick -> navigateTo(ProjectEditView.VIEW_NAME, itemClick.getItem().getId()));

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
