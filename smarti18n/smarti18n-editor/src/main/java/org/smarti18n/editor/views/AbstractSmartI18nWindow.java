package org.smarti18n.editor.views;

import com.vaadin.ui.Window;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
abstract class AbstractSmartI18nWindow extends Window {

    AbstractSmartI18nWindow(final String caption) {
        super(caption);

        setWidth(30, Unit.EM);
        setResizable(false);
        setModal(true);

        center();
    }

    String translate(final String code, final String... args) {
        return I18N.translate(code, args);
    }

}
