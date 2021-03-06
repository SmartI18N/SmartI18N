package org.smarti18n.vaadin.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class MarginLayout extends VerticalLayout {

    public MarginLayout(final Component component) {
        setSizeFull();
        setMargin(true);

        addComponent(component);
    }
}
