package org.smarti18n.admin.uis;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.smarti18n.admin.views.ProjectsView;
import org.smarti18n.admin.views.StartView;
import org.smarti18n.admin.views.UsersView;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringUI(path = "/")
@Theme("smarti18n")
@StyleSheet({
        "http://fonts.googleapis.com/css?family=Questrial"
})
@Viewport("initial-scale=1, maximum-scale=1")
public class AdminUI extends UI {

    private final SpringViewProvider viewProvider;


    public AdminUI(final SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;

        getPage().setTitle(I18N.translate("smarti18n.admin.title"));
    }

    @Override
    protected void init(final VaadinRequest vaadinRequest) {

        final MenuBar menuBar = new MenuBar();
        menuBar.setWidth(100, Unit.PERCENTAGE);
        menuBar.addItem(I18N.translate("smarti18n.admin.menu.start"), menuItem -> getNavigator().navigateTo(StartView.VIEW_NAME));
        menuBar.addItem(I18N.translate("smarti18n.admin.menu.users"), menuItem -> getNavigator().navigateTo(UsersView.VIEW_NAME));
        menuBar.addItem(I18N.translate("smarti18n.admin.menu.projects"), menuItem -> getNavigator().navigateTo(ProjectsView.VIEW_NAME));


        final VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        final VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        contentLayout.addComponent(menuBar);
        contentLayout.addComponent(content);

        contentLayout.setExpandRatio(content, 1);

        setContent(contentLayout);

        final Navigator navigator = new Navigator(this, content);
        navigator.addProvider(viewProvider);
    }

}
