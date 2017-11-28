package org.smarti18n.vaadin.components;

import com.vaadin.icons.VaadinIcons;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class CancelButton extends IconButton {

    public CancelButton(final ClickListener listener) {
        super(I18N.translate("common.cancel"), VaadinIcons.EXIT, listener);
    }
}
