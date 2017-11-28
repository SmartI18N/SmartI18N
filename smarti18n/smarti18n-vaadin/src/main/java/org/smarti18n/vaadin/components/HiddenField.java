package org.smarti18n.vaadin.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class HiddenField extends CustomField<String> {

    private String value;

    @Override
    protected Component initContent() {
        return null;
    }

    @Override
    protected void doSetValue(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
