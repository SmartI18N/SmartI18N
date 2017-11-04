package org.smarti18n.editor;

import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.impl.MessagesApiImpl;
import org.smarti18n.api.spring.Smarti18nMessageSource;
import org.smarti18n.editor.vaadin.I18N;
import org.smarti18n.editor.views.MessageImportView;
import org.smarti18n.editor.views.MessageOverviewView;
import org.smarti18n.editor.views.ProfileView;
import org.smarti18n.editor.views.StartView;
import org.smarti18n.editor.views.StatisticView;

import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
        return new MessagesApiImpl(environment, new RestTemplateBuilder().build());
    }

    @Bean
    Smarti18nMessageSource messageSource(final MessagesApi messagesApi) {
        return new Smarti18nMessageSource(messagesApi);
    }

    @SpringUI
    @Theme("valo")
    public static class EditorUI extends UI {

        private final I18N i18N;
        private final SpringViewProvider viewProvider;


        public EditorUI(final I18N i18N, final SpringViewProvider viewProvider) {
            this.i18N = i18N;
            this.viewProvider = viewProvider;
        }

        @Override
        protected void init(final VaadinRequest vaadinRequest) {
            final SideMenu sideMenu = new SideMenu();
            sideMenu.setMenuCaption(this.i18N.getMessage("smarti18n.editor.title"));

            sideMenu.setUserName("Marc Bellmann");

            sideMenu.addMenuItem(this.i18N.getMessage("smarti18n.editor.menu.start"), VaadinIcons.HOME, navigateTo(StartView.VIEW_NAME));
            sideMenu.addMenuItem(this.i18N.getMessage("smarti18n.editor.menu.message-overview"), VaadinIcons.LIST, navigateTo(MessageOverviewView.VIEW_NAME));
            sideMenu.addMenuItem(this.i18N.getMessage("smarti18n.editor.menu.message-import"), VaadinIcons.UPLOAD, navigateTo(MessageImportView.VIEW_NAME));
            sideMenu.addMenuItem(this.i18N.getMessage("smarti18n.editor.menu.profile"), VaadinIcons.TOOLS, navigateTo(ProfileView.VIEW_NAME));
            sideMenu.addMenuItem(this.i18N.getMessage("smarti18n.editor.menu.statistic"), VaadinIcons.BAR_CHART, navigateTo(StatisticView.VIEW_NAME));

            setContent(sideMenu);

            final Navigator navigator = new Navigator(this, sideMenu);
            navigator.addProvider(viewProvider);
        }

        private SideMenu.MenuClickHandler navigateTo(final String viewName) {
            return () -> getUI().getNavigator().navigateTo(viewName);
        }

    }
}
