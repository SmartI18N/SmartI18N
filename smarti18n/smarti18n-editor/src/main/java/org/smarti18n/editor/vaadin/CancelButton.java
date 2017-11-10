package org.smarti18n.editor.vaadin;

import com.vaadin.icons.VaadinIcons;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class CancelButton extends IconButton {

    public CancelButton(final ClickListener listener) {
        super(I18N.getMessage("common.cancel"), VaadinIcons.EXIT, listener);
    }
}
