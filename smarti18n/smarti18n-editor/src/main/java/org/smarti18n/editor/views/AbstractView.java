package org.smarti18n.editor.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;

import org.smarti18n.editor.utils.I18N;
import org.smarti18n.editor.vaadin.VaadinUtils;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
abstract class AbstractView extends VerticalLayout implements View {

    AbstractView() {
        setPrimaryStyleName("view");
    }

    void init(final String caption) {
        setCaption(caption);
    }

    String translate(final String code, final String... args) {
        return I18N.getMessage(code, args);
    }

    void navigateTo(final String viewId, final String... params) {
        VaadinUtils.navigateTo(viewId, params);
    }
}
