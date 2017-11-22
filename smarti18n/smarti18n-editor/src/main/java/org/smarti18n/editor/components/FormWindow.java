package org.smarti18n.editor.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

public class FormWindow extends Window {

    private final FormLayout form;

    public FormWindow(final String caption) {
        super(caption);

        setWidth(30, Unit.EM);
        setResizable(false);
        setModal(true);

        center();

        this.form = new FormLayout();
        this.form.setMargin(true);

        setContent(this.form);
    }

    public void addFormComponent(final Component formComponent) {
        formComponent.setSizeFull();
        this.form.addComponent(formComponent);
    }

    public void addFormButtons(final Component... buttons) {
        this.form.addComponent(new HorizontalLayout(buttons));
    }
}
