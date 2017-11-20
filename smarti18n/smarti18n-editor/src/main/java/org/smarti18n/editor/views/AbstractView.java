package org.smarti18n.editor.views;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
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

        final Label captionLabel = new Label("<h2>" + caption + "</h2>", ContentMode.HTML);
        captionLabel.setPrimaryStyleName("view-caption");
        addComponent(captionLabel);

    }

    String translate(final String code, final String... args) {
        return I18N.getMessage(code, args);
    }

    void navigateTo(final String viewId, final String... params) {
        VaadinUtils.navigateTo(viewId, params);
    }
}
