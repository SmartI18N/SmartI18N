package org.smarti18n.editor.components;

import com.vaadin.icons.VaadinIcons;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class SaveButton extends IconButton {

    public SaveButton(final ClickListener listener) {
        super(I18N.translate("common.save"), VaadinIcons.LOCK, listener);
    }
}
