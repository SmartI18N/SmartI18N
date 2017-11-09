package org.smarti18n.editor.views;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectOverviewView.VIEW_NAME)
public class ProjectOverviewView extends AbstractView implements View {

    public static final String VIEW_NAME = "project/overview";

    private final ProjectsApi projecApi;

    private Grid<Project> grid;

    public ProjectOverviewView(final I18N i18N, final ProjectsApi projecApi) {
        super(i18N);

        this.projecApi = projecApi;
    }

    @PostConstruct
    void init() {
        setCaption(translate("smarti18n.editor.project-overview.caption"));

        grid = new Grid<>(Project.class);

        grid.setColumns("name");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getId();
            navigator().navigateTo(ProjectEditView.VIEW_NAME + "/" + key);
        });

        addComponent(grid);

        setSizeFull();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        grid.setItems(loadProjects());
    }

    private List<Project> loadProjects() {
        return this.projecApi.findAll().stream().collect(Collectors.toList());
    }
}
