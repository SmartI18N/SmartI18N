package org.smarti18n.editor.views;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.vaadin.utils.ProjectContext;

abstract class AbstractProjectView extends AbstractView {

    final ProjectContext projectContext = new ProjectContext();

    AbstractProjectView(final EditorController editorController) {
        super(editorController);
    }

    @Override
    void init(final String caption) {
        final MenuBar menuBar = new MenuBar();
        menuBar.setSizeUndefined();
        menuBar.setWidth(100, Unit.PERCENTAGE);

        menuBar.addItem(translate("smarti18n.editor.project-menu.messages"), navigateToProjectView(ProjectMessagesView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.translator"), navigateToProjectView(ProjectTranslatorView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.import-export"), navigateToProjectView(ProjectImportExportView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.locales"), navigateToProjectView(ProjectLocalesView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.users"), navigateToProjectView(ProjectUsersView.VIEW_NAME));
        menuBar.addItem(translate("smarti18n.editor.project-menu.settings"), navigateToProjectView(ProjectGeneralView.VIEW_NAME));
        addComponent(menuBar);

        super.init(caption);

        final HorizontalLayout buttonBar = createButtonBar();
        if (buttonBar != null) {
            addComponent(buttonBar);
        }
    }

    private MenuBar.Command navigateToProjectView(final String viewName) {
        return selectedItem -> navigateTo(viewName, this.projectContext.getProjectId());
    }

    protected HorizontalLayout createButtonBar() {
        return null;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final String projectId = event.getParameters();

        this.projectContext.setProject(
                this.editorController.getProject(projectId)
        );
    }
}
