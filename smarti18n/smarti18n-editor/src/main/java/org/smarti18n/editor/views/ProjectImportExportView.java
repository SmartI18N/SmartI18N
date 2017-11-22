package org.smarti18n.editor.views;

import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.gateway.ImportExportHandler;
import org.smarti18n.editor.utils.I18N;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import javax.annotation.PostConstruct;
import java.util.List;

@UIScope
@SpringView(name = ProjectImportExportView.VIEW_NAME)
public class ProjectImportExportView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/import-export";

    private Grid<ImportExportHandler> grid;

    private final List<ImportExportHandler> importExportHandlerList;

    ProjectImportExportView(final ProjectsApi projectsApi, final List<ImportExportHandler> importExportHandlerList) {
        super(projectsApi);
        this.importExportHandlerList = importExportHandlerList;
    }

    @PostConstruct
    public void init() {
        super.init(I18N.translate("smarti18n.editor.project-import-export.caption"));
        setSizeFull();

        grid = new Grid<>(ImportExportHandler.class);

        grid.setColumns();
        grid.addColumn(ImportExportHandler::getName)
                .setCaption(translate("smarti18n.editor.project-import-export.name"));

        grid.addComponentColumn(handler -> {
            if (handler.hasImport()) {
                return new Button(
                        translate("smarti18n.editor.project-import-export.import"),
                        event -> getUI().addWindow(handler.createImportWindow(this.projectContext.getProject()))
                );
            } else {
                return new Label("-");
            }
        }).setCaption(translate("smarti18n.editor.project-import-export.import"));

        grid.addComponentColumn(handler -> {
            if (handler.hasExport()) {
                return new Button(
                        translate("smarti18n.editor.project-import-export.export"),
                        event -> getUI().addWindow(handler.createExportWindow(this.projectContext.getProject()))
                );
            } else {
                return new Label("-");
            }
        }).setCaption(translate("smarti18n.editor.project-import-export.export"));

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        grid.setItems(this.importExportHandlerList);

        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    @Override
    protected HorizontalLayout createButtonBar() {
        return new HorizontalLayout();
    }
}
