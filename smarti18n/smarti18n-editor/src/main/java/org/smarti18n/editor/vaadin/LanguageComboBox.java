package org.smarti18n.editor.vaadin;

import java.util.Arrays;
import java.util.Locale;

import com.vaadin.ui.ComboBox;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class LanguageComboBox extends ComboBox<Locale> {

    public LanguageComboBox(final String caption) {
        super(caption, Arrays.asList(Locale.GERMAN, Locale.ENGLISH, Locale.ITALIAN));
    }
}
