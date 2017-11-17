package org.smarti18n.editor.components;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Layout;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface Tab extends Layout {
    void init();

    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent);
}
