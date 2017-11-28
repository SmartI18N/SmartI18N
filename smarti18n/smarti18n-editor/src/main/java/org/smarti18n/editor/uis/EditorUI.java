package org.smarti18n.editor.uis;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

import org.smarti18n.api.ProjectsApi;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.editor.views.ProjectCreateWindow;
import org.smarti18n.editor.views.ProjectMessagesView;
import org.smarti18n.editor.views.ProfileView;
import org.smarti18n.editor.views.StartView;

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

    private final ProjectsApi projectsApi;


    public EditorUI(
            final SpringViewProvider viewProvider,
            final VaadinSecurity vaadinSecurity,
            final ProjectsApi projectsApi) {

        this.viewProvider = viewProvider;
        this.vaadinSecurity = vaadinSecurity;
        this.projectsApi = projectsApi;

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

        projectsApi.findAll().forEach(project -> sideMenu.addNavigation(
                project.getDisplayName(),
                VaadinIcons.LIST,
                ProjectMessagesView.VIEW_NAME + "/" + project.getId()
        ));

        sideMenu.addMenuItem(I18N.translate("smarti18n.editor.menu.new-project"), VaadinIcons.FOLDER_ADD, () -> addWindow(new ProjectCreateWindow(this.projectsApi)));

        sideMenu.addNavigation(I18N.translate("smarti18n.editor.menu.profile"), VaadinIcons.USER, ProfileView.VIEW_NAME);

        sideMenu.addMenuItem(I18N.translate("common.logout"), VaadinIcons.EXIT, this.vaadinSecurity::logout);

        setContent(sideMenu);

        final Navigator navigator = new Navigator(this, sideMenu);
        navigator.addProvider(viewProvider);
    }

}
