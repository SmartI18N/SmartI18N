package org.smarti18n.editor.vaadin;

import java.util.Arrays;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public abstract class AbstractView extends VerticalLayout implements View {

    protected String translate(final String code, final String... args) {
        return I18N.getMessage(code,args);
    }

    protected void navigateTo(final String viewId, final String ... params) {
        final StringBuilder navigationState = new StringBuilder(viewId);
        Arrays.asList(params).forEach(
                param -> navigationState.append("/").append(param)
        );

        this.getUI().getNavigator().navigateTo(navigationState.toString());
    }
}
