package org.smarti18n.vaadin.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

public class LabelField extends CustomField<String> {

    private final Label label = new Label();

    @Override
    protected Component initContent() {
        this.label.setSizeFull();
        return this.label;
    }

    @Override
    protected void doSetValue(final String value) {
        this.label.setValue(value);
    }

    @Override
    public String getValue() {
        return this.label.getValue();
    }
}
