package org.smarti18n.editor.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProfileView.VIEW_NAME)
public class ProfileView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "profile";

    @PostConstruct
    private void init() {

    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        addComponent(new Label("ProfileView"));
    }

}