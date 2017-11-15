package org.smarti18n.editor.components;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.vaadin.VaadinUtils;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public abstract class AbstractView extends VerticalLayout implements View {

    protected String translate(final String code, final String... args) {
        return I18N.getMessage(code,args);
    }

    protected void navigateTo(final String viewId, final String ... params) {
        this.getUI().getNavigator().navigateTo(VaadinUtils.buildNavigation(viewId, params));
    }
}
