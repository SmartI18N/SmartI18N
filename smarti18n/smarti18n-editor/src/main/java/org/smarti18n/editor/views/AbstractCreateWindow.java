package org.smarti18n.editor.views;

import com.vaadin.ui.Window;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
abstract class AbstractCreateWindow extends Window {

    AbstractCreateWindow(final String caption) {
        super(caption);

        setWidth(30, Unit.EM);
        setResizable(false);

        center();
    }
}
