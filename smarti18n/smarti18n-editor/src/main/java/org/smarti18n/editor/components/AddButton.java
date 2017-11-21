package org.smarti18n.editor.components;

import com.vaadin.icons.VaadinIcons;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class AddButton extends IconButton {

    public AddButton(final ClickListener listener) {
        super(I18N.translate("common.add"), VaadinIcons.PLUS, listener);
    }
}
