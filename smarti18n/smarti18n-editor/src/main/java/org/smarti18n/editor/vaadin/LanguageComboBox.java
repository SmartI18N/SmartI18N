package org.smarti18n.editor.vaadin;

import com.vaadin.ui.ComboBox;

import java.util.Arrays;
import java.util.Locale;

public class LanguageComboBox extends ComboBox<Locale> {

    public LanguageComboBox(final String caption) {
        super(caption, Arrays.asList(Locale.GERMAN, Locale.ENGLISH, Locale.ITALIAN));
    }
}
