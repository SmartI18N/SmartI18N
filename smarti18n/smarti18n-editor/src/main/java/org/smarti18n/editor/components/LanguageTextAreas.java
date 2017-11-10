package org.smarti18n.editor.components;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class LanguageTextAreas extends CustomField<Map<Locale, String>> {

    private final Layout fields = new FormLayout();
    private final Map<Locale, TextArea> textAreas = new HashMap<>();

    @Override
    protected Component initContent() {
        return fields;
    }

    @Override
    protected void doSetValue(final Map<Locale, String> value) {
        this.textAreas.clear();
        this.fields.removeAllComponents();

        for (final Map.Entry<Locale, String> entry : value.entrySet()) {
            final TextArea textArea = new TextArea(entry.getKey().getLanguage());
            textArea.setSizeFull();
            textArea.setValue(entry.getValue());

            this.textAreas.put(entry.getKey(), textArea);
            this.fields.addComponent(textArea);
        }
    }

    @Override
    public Map<Locale, String> getValue() {
        return this.textAreas.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, p -> p.getValue().getValue()));
    }
}
