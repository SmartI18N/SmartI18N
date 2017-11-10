package org.smarti18n.editor.vaadin;

import com.vaadin.icons.VaadinIcons;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class SaveButton extends IconButton {

    public SaveButton(final ClickListener listener) {
        super(I18N.getMessage("common.save"), VaadinIcons.LOCK, listener);
    }
}
