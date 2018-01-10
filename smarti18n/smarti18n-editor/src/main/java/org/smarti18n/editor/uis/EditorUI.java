package org.smarti18n.editor.uis;

import java.util.Comparator;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.editor.views.ProfileView;
import org.smarti18n.editor.views.ProjectCreateWindow;
import org.smarti18n.editor.views.ProjectMessagesView;
import org.smarti18n.editor.views.StartView;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Project;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.vaadin.utils.VaadinExceptionHandler;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.teemusa.sidemenu.SideMenu;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringUI(path = "/")
@Theme("smarti18n")
@StyleSheet({
        "http://fonts.googleapis.com/css?family=Questrial"
})
@Viewport("initial-scale=1, maximum-scale=1")
public class EditorUI extends UI {

    private final SpringViewProvider viewProvider;
    private final VaadinSecurity vaadinSecurity;

    private final EditorController editorController;


    public EditorUI(
            final SpringViewProvider viewProvider,
            final VaadinSecurity vaadinSecurity,
            final EditorController editorController) {

        this.viewProvider = viewProvider;
        this.vaadinSecurity = vaadinSecurity;
        this.editorController = editorController;

        getPage().setTitle(I18N.translate("smarti18n.editor.title"));
    }

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        final SideMenu sideMenu = new SideMenu();
        sideMenu.setMenuCaption(I18N.translate("smarti18n.editor.title"));

        sideMenu.setUserName("Marc Bellmann");
        sideMenu.setUserMenuVisible(false);
        sideMenu.setIcon(null);

        sideMenu.addNavigation(I18N.translate("smarti18n.editor.menu.start"), VaadinIcons.HOME, StartView.VIEW_NAME);

        try {
            editorController.getProjects().stream()
                    .sorted(Comparator.comparing(Project::getDisplayName))
                    .forEach(project -> sideMenu.addNavigation(
                            project.getDisplayName(),
                            VaadinIcons.FOLDER,
                            ProjectMessagesView.VIEW_NAME + "/" + project.getId()
                    ));
        } catch (UserUnknownException e) {
            VaadinExceptionHandler.handleUserUnknownException();
            throw new IllegalStateException(e);
        }

        sideMenu.addMenuItem(I18N.translate("smarti18n.editor.menu.new-project"), VaadinIcons.FOLDER_ADD, () -> addWindow(new ProjectCreateWindow(this.editorController)));

        sideMenu.addNavigation(I18N.translate("smarti18n.editor.menu.profile"), VaadinIcons.USER, ProfileView.VIEW_NAME);

        sideMenu.addMenuItem(I18N.translate("common.logout"), VaadinIcons.EXIT, this.vaadinSecurity::logout);

        setContent(sideMenu);

        final Navigator navigator = new Navigator(this, sideMenu);
        navigator.addProvider(viewProvider);
    }

}
