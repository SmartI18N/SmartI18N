package org.smarti18n.editor.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class IconButton extends Button {

    public IconButton(final String caption, final Resource icon, final ClickListener listener) {
        super(caption, listener);
        setIcon(icon);
    }

    public IconButton(final VaadinIcons icon, final ClickListener listener) {
        super(icon);
        addStyleNames(ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_TINY);
        addClickListener(listener);
    }

    public IconButton(final String caption, final VaadinIcons icon) {
        super(caption);

        setIcon(icon);
    }
}
