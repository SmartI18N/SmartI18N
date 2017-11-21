package org.smarti18n.editor.views;

import java.util.Locale;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectLocalesView.VIEW_NAME)
public class ProjectLocalesView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/locales";

    private Grid<Locale> grid;

    ProjectLocalesView(final ProjectsApi projectsApi) {
        super(projectsApi);
    }

    @PostConstruct
    public void init() {
        super.init(I18N.translate("smarti18n.editor.project-locales.caption"));
        setSizeFull();

        grid = new Grid<>(Locale.class);
        grid.setColumns("displayLanguage", "language", "displayCountry", "country");
        grid.addComponentColumn(locale -> new IconButton(VaadinIcons.MINUS, clickEvent -> {
            final Project project = projectsApi.findOne(this.projectContext.getProjectId());
            project.getLocales().remove(locale);
            projectsApi.update(project);

            reloadGrid();
        }));

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    @Override
    protected HorizontalLayout createButtonBar() {
        final IconButton newLocaleButton = new IconButton(
                translate("smarti18n.editor.project-locales.add-new-locale"),
                VaadinIcons.FILE_ADD,
                clickEvent -> {
                    final ProjectLocaleAddWindow window = new ProjectLocaleAddWindow(
                            this.projectsApi,
                            this.projectContext.getProjectId()
                    );
                    window.addCloseListener(closeEvent -> reloadGrid());

                    this.getUI().addWindow(window);
                });

        return new HorizontalLayout(newLocaleButton);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);

        reloadGrid();
    }

    private void reloadGrid() {
        final Project project = this.projectContext.getProject();

        grid.setItems(
                project.getLocales()
        );
    }

}
