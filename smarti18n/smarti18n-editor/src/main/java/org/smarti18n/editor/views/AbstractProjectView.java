package org.smarti18n.editor.views;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.vaadin.utils.ProjectContext;

abstract class AbstractProjectView extends AbstractView {

    final ProjectContext projectContext = new ProjectContext();

    protected final ProjectsApi projectsApi;

    AbstractProjectView(final ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;
    }

    @Override
    void init(final String caption) {
        final MenuBar menuBar = new MenuBar();
        menuBar.setSizeUndefined();
        menuBar.setWidth(100, Unit.PERCENTAGE);

        menuBar.addItem(translate("smarti18n.editor.project-menu.messages"), navigateToProjectView(ProjectMessagesView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.import-export"), navigateToProjectView(ProjectImportExportView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.locales"), navigateToProjectView(ProjectLocalesView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.users"), navigateToProjectView(ProjectUsersView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.settings"), navigateToProjectView(ProjectGeneralView.VIEW_NAME));
        addComponent(menuBar);

        super.init(caption);

        addComponent(createButtonBar());
    }

    private MenuBar.Command navigateToProjectView(final String viewName) {
        return selectedItem -> navigateTo(viewName, this.projectContext.getProjectId());
    }

    protected abstract HorizontalLayout createButtonBar();

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final String projectId = event.getParameters();

        loadProjectContext(projectId);
    }

    void loadProjectContext(final String projectId) {
        final Project project = this.projectsApi.findOne(projectId);

        this.projectContext.setProject(project);
    }
}
