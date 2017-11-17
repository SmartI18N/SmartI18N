package org.smarti18n.editor.components;

import java.util.Collection;
import java.util.Locale;

import com.vaadin.ui.ComboBox;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class LocaleComboBox extends ComboBox<Locale> {

    public LocaleComboBox(final String caption, final Collection<Locale> locales) {
        super(caption, locales);
    }
}
