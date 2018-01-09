package org.smarti18n.admin.views;

import java.util.List;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.vaadin.components.AddButton;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.components.IconButton;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
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
        super.init(
                translate("smarti18n.admin.projects.caption"),
                newProjectButton()
        );

        setSizeFull();

        this.grid = new Grid<>(Project.class);
        this.grid.setSizeFull();
        this.grid.setColumns("displayName", "locales");

        addComponent(this.grid);
        setExpandRatio(this.grid, 1);
    }

    private IconButton newProjectButton() {
        return new IconButton(
                translate("smarti18n.admin.projects.add-new-project"),
                VaadinIcons.FILE_ADD,
                e -> {

                    final FormWindow window = new FormWindow(
                            translate("smarti18n.admin.projects.add-new-project")
                    );

                    window.setModal(true);

                    final TextField textFieldKey = new TextField(I18N.translate("smarti18n.editor.message-create.key"));
                    window.addFormComponent(textFieldKey);

                    final AddButton addButton = new AddButton(clickEvent -> {
                        this.projectsApi.insert(textFieldKey.getValue());

                        enter(null);
                        window.close();
                    });
                    final CancelButton cancelButton = new CancelButton(clickEvent -> window.close());

                    window.addFormButtons(addButton, cancelButton);

                    getUI().addWindow(window);
                }
        );
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final List<Project> projects = this.projectsApi.findAll();

        grid.setItems(projects);
    }
}
