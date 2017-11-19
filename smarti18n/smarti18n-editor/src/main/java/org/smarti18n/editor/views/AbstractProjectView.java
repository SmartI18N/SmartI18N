package org.smarti18n.editor.views;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.utils.ProjectContext;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;

class AbstractProjectView extends AbstractView {

    protected final ProjectContext projectContext = new ProjectContext();

    private final ProjectsApi projectsApi;

    AbstractProjectView(final ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;
    }

    @Override
    void init(final String caption) {
        final MenuBar menuBar = new MenuBar();
        menuBar.setSizeUndefined();
        menuBar.setWidth(100, Unit.PERCENTAGE);
        menuBar.addItem("Messages", selectedItem -> navigateTo(ProjectMessagesView.VIEW_NAME, this.projectContext.getProjectId()));
        menuBar.addItem("Import / Export", selectedItem -> navigateTo(ProjectMessagesView.VIEW_NAME, this.projectContext.getProjectId()));
        menuBar.addItem("Locales", selectedItem -> navigateTo(ProjectLocalesView.VIEW_NAME, this.projectContext.getProjectId()));
        menuBar.addItem("Users", selectedItem -> navigateTo(ProjectUsersView.VIEW_NAME, this.projectContext.getProjectId()));
        menuBar.addItem("General", selectedItem -> navigateTo(ProjectGeneralView.VIEW_NAME, this.projectContext.getProjectId()));
        addComponent(menuBar);

        final Label captionLabel = new Label("<h2>" + caption + "</h2>", ContentMode.HTML);
        captionLabel.setPrimaryStyleName("view-caption");
        addComponent(captionLabel);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final Project project = this.projectsApi.findOne(event.getParameters());

        this.projectContext.setProject(project);
    }
}
