package org.smarti18n.editor.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractView extends VerticalLayout implements View {

    protected String translate(final String code) {
        return I18N.getMessage(code);
    }

    protected Navigator navigator() {
        return this.getUI().getNavigator();
    }
}
