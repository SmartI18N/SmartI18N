package org.smarti18n.editor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.ProjectsApiImpl;
import org.smarti18n.api.spring.Smarti18nMessageSource;
import org.smarti18n.editor.vaadin.I18N;
import org.smarti18n.editor.views.MessageOverviewView;
import org.smarti18n.editor.views.ProjectOverviewView;
import org.smarti18n.editor.views.StartView;
import org.vaadin.teemusa.sidemenu.SideMenu;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringBootApplication(
        scanBasePackages = "org.smarti18n.editor"
)
@EnableAutoConfiguration
@Configuration
public class EditorApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EditorApplication.class, args);
    }

    @Bean
    MessagesApi messagesApi(final Environment environment) {
        return new MessagesApiImpl(environment, restTemplate());
    }

    @Bean
    ProjectsApi projectsApi(final Environment environment) {
        return new ProjectsApiImpl(environment, restTemplate());
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    Smarti18nMessageSource messageSource(final Environment environment) {
        return new Smarti18nMessageSource(
                environment.getProperty("smarti18n.host", MessagesApiImpl.DEFAULT_HOST)
        );
    }

    @SpringUI
    @Theme("smarti18n")
    public static class EditorUI extends UI {

        private final I18N i18N;
        private final SpringViewProvider viewProvider;

        private final ProjectsApi projectsApi;


        public EditorUI(final I18N i18N, final SpringViewProvider viewProvider, final ProjectsApi projectsApi) {
            this.i18N = i18N;
            this.viewProvider = viewProvider;
            this.projectsApi = projectsApi;
        }

        @Override
        protected void init(final VaadinRequest vaadinRequest) {
            final SideMenu sideMenu = new SideMenu();
            sideMenu.setMenuCaption(this.i18N.getMessage("smarti18n.editor.title"));

            sideMenu.setUserName("Marc Bellmann");
            sideMenu.setUserMenuVisible(false);
            sideMenu.setIcon(null);

            sideMenu.addNavigation(this.i18N.getMessage("smarti18n.editor.menu.start"), VaadinIcons.HOME, StartView.VIEW_NAME);
            sideMenu.addNavigation(this.i18N.getMessage("smarti18n.editor.menu.projects"), VaadinIcons.PAINT_ROLL, ProjectOverviewView.VIEW_NAME);
//            sideMenu.addNavigation(this.i18N.getMessage("smarti18n.editor.menu.profile"), VaadinIcons.TOOLS, ProfileView.VIEW_NAME);

            projectsApi.findAll().forEach(project -> sideMenu.addNavigation(
                    project.getName() + " / " + this.i18N.getMessage("smarti18n.editor.menu.messages"),
                    VaadinIcons.LIST,
                    MessageOverviewView.VIEW_NAME
            ));

//            sideMenu.addNavigation(this.i18N.getMessage("smarti18n.editor.menu.statistic"), VaadinIcons.BAR_CHART, StatisticView.VIEW_NAME);

            setContent(sideMenu);

            final Navigator navigator = new Navigator(this, sideMenu);
            navigator.addProvider(viewProvider);
        }

        private SideMenu.MenuClickHandler navigateTo(final String viewName) {
            return () -> getUI().getNavigator().navigateTo(viewName);
        }

    }
}
