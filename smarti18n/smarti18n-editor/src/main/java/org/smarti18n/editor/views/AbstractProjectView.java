package org.smarti18n.editor.views;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.utils.ProjectContext;

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
        menuBar.addItem("Messages", selectedItem -> navigateTo(ProjectMessagesView.VIEW_NAME, this.projectContext.getProjectId()));
//        menuBar.addItem("Import / Export", selectedItem -> navigateTo(ProjectMessagesView.VIEW_NAME, this.projectContext.getProjectId()));
        menuBar.addItem("Locales", selectedItem -> navigateTo(ProjectLocalesView.VIEW_NAME, this.projectContext.getProjectId()));
        menuBar.addItem("Users", selectedItem -> navigateTo(ProjectUsersView.VIEW_NAME, this.projectContext.getProjectId()));
        menuBar.addItem("Settings", selectedItem -> navigateTo(ProjectGeneralView.VIEW_NAME, this.projectContext.getProjectId()));
        addComponent(menuBar);

        super.init(caption);

        addComponent(createButtonBar());
    }

    protected abstract HorizontalLayout createButtonBar();

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final String projectId = event.getParameters();

        loadProjectContext(projectId);
    }

    protected void loadProjectContext(final String projectId) {
        final Project project = this.projectsApi.findOne(projectId);

        this.projectContext.setProject(project);
    }
}
