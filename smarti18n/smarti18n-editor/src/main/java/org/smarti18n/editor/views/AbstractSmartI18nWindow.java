package org.smarti18n.editor.views;

import com.vaadin.ui.Window;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
abstract class AbstractSmartI18nWindow extends Window {

    AbstractSmartI18nWindow(final String caption) {
        super(caption);

        setWidth(30, Unit.EM);
        setResizable(false);

        center();
    }
}