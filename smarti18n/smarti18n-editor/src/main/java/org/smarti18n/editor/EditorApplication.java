package org.smarti18n.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.smarti18n.editor.views.MessagesOverviewView;
import org.smarti18n.editor.views.ProfileView;
import org.smarti18n.editor.views.StartView;
import org.vaadin.teemusa.sidemenu.SideMenu;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringBootApplication(
        scanBasePackages = "org.smarti18n.editor"
)
@EnableAutoConfiguration
public class EditorApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EditorApplication.class, args);
    }

    @SpringUI
    @Theme("valo")
    public static class EditorUI extends UI {

        @Autowired
        private SpringViewProvider viewProvider;

        @Override
        protected void init(final VaadinRequest vaadinRequest) {
            final SideMenu sideMenu = new SideMenu();
            sideMenu.setMenuCaption("SmartI18N Editor");

            sideMenu.setUserName("Marc Bellmann");

            sideMenu.addMenuItem("StartView", navigateTo(StartView.VIEW_NAME));
            sideMenu.addMenuItem("MessagesOverviewView", navigateTo(MessagesOverviewView.VIEW_NAME));
            sideMenu.addMenuItem("ProfileView", navigateTo(ProfileView.VIEW_NAME));

            setContent(sideMenu);

            final Navigator navigator = new Navigator(this, sideMenu);
            navigator.addProvider(viewProvider);
        }

        private SideMenu.MenuClickHandler navigateTo(final String viewName) {
            return () -> getUI().getNavigator().navigateTo(viewName);
        }

    }
}
