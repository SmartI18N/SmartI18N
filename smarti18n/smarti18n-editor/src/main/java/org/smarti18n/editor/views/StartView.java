package org.smarti18n.editor.views;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import javax.annotation.PostConstruct;
import org.smarti18n.editor.components.AbstractView;
import org.smarti18n.editor.security.SimpleUserDetails;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = StartView.VIEW_NAME)
public class StartView extends AbstractView implements View {

    public static final String VIEW_NAME = "";

    public StartView() {

    }

    @PostConstruct
    private void init() {
        setCaption(translate("smarti18n.editor.start.caption"));
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final SimpleUserDetails principal = (SimpleUserDetails) authentication.getPrincipal();
        final String username = principal.getName();

        addComponent(new Label(translate("smarti18n.editor.start.welcome", username)));

        addComponent(new Label(translate("smarti18n.editor.start.which-language", getLocale().toString())));
        addComponent(new Label(getLocale().toString()));
    }

}
