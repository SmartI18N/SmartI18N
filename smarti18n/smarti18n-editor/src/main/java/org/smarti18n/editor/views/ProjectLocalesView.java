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
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.Project;
import org.smarti18n.vaadin.components.IconButton;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectLocalesView.VIEW_NAME)
public class ProjectLocalesView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/locales";

    private Grid<Locale> grid;

    ProjectLocalesView(final EditorController editorController) {
        super(editorController);
    }

    @PostConstruct
    public void init() {
        super.init(I18N.translate("smarti18n.editor.project-locales.caption"));
        setSizeFull();

        grid = new Grid<>(Locale.class);
        grid.setColumns("displayLanguage", "language", "displayCountry", "country");
        grid.addComponentColumn(locale -> new IconButton(
                VaadinIcons.MINUS,
                this.editorController.clickRemoveLocale(projectContext, locale, this::reloadGrid)
        ));

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
                            this.editorController,
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
        final Project project = this.editorController.getProject(
                this.projectContext.getProjectId()
        );

        grid.setItems(
                project.getLocales()
        );
    }

}
