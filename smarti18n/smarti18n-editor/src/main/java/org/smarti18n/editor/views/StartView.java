package org.smarti18n.editor.views;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.vaadin.components.IconButton;
import org.smarti18n.vaadin.security.SimpleUserDetails;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = StartView.VIEW_NAME)
public class StartView extends AbstractView implements View {

    public static final String VIEW_NAME = "";

    public StartView(final EditorController editorController) {
        super(editorController);
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.start.caption"));
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        removeAllComponents();

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final SimpleUserDetails principal = (SimpleUserDetails) authentication.getPrincipal();
        final String username = principal.getUsername();

        final Panel welcome = new Panel(translate("smarti18n.editor.welcome.caption", username));
        welcome.setContent(new VerticalLayout(
                new Label(translate("smarti18n.editor.welcome.text", username))
        ));
        addComponent(welcome);

        final Panel newProject = new Panel(I18N.translate("smarti18n.editor.start.new-project.caption"));
        newProject.setContent(new VerticalLayout(
                new Label(I18N.translate("smarti18n.editor.start.new-project.text")),
                new IconButton(
                        translate("smarti18n.editor.project-overview.add-new-project"),
                        VaadinIcons.FILE_ADD,
                        clickEvent -> this.getUI().addWindow(new ProjectCreateWindow(this.editorController))
                )
        ));
        addComponent(newProject);

    }

}
