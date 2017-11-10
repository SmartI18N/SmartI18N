package org.smarti18n.editor.components;

import java.util.LinkedHashSet;
import java.util.Set;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class LabelSet extends CustomField<Set<String>> {

    private VerticalLayout layout;

    public LabelSet(final String caption) {
        setCaption(caption);
        setSizeFull();
    }

    @Override
    protected Component initContent() {
        this.layout = new VerticalLayout();
        return layout;
    }

    @Override
    protected void doSetValue(final Set<String> value) {
        value.forEach(string -> layout.addComponent(new Label(string)));
    }

    @Override
    public Set<String> getValue() {
        final Set<String> values = new LinkedHashSet<>();
        this.layout.forEach(component -> {
            if (component instanceof Label) {
                values.add(((Label) component).getValue());
            }
        });
        return values;
    }
}