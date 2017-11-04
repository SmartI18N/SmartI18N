package org.smarti18n.editor.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractView extends VerticalLayout implements View {

    private final I18N i18N;

    protected AbstractView(final I18N i18N) {
        this.i18N = i18N;
    }

    protected String translate(final String code) {
        return this.i18N.getMessage(code);
    }

    protected void refreshMessageSource() {
        this.i18N.refreshMessageSource();
    }

}
