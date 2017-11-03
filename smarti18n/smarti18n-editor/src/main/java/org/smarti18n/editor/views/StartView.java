package org.smarti18n.editor.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = StartView.VIEW_NAME)
public class StartView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "";

    @PostConstruct
    private void init() {
        setCaption("Welcome");
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
