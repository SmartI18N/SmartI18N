package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.smarti18n.api.Project;
import org.smarti18n.editor.components.Tab;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
class ProjectEditGeneralTab extends FormLayout implements Tab {

    private final Binder<Project> binder;

    ProjectEditGeneralTab(final Binder<Project> binder) {
        this.binder = binder;
    }

    @Override
    public void init() {
        setSizeFull();
        setMargin(true);

        final TextField textFieldId = new TextField(I18N.getMessage("smarti18n.editor.project-edit.id"));
        textFieldId.setReadOnly(true);
        textFieldId.setSizeFull();
        addComponent(textFieldId);

        final TextField textFieldName = new TextField(I18N.getMessage("smarti18n.editor.project-edit.name"));
        textFieldName.setSizeFull();
        addComponent(textFieldName);

        final TextArea textAreaDescription = new TextArea(I18N.getMessage("smarti18n.editor.project-edit.description"));
        textAreaDescription.setSizeFull();
        addComponent(textAreaDescription);

        final TextField textFieldSecret = new TextField(I18N.getMessage("smarti18n.editor.project-edit.secret"));
        textFieldSecret.setReadOnly(true);
        textFieldSecret.setSizeFull();
        addComponent(textFieldSecret);

        this.binder.forMemberField(textFieldId).bind("id");
        this.binder.forMemberField(textFieldName).bind("name");
        this.binder.forMemberField(textAreaDescription).bind("description");
        this.binder.forMemberField(textFieldSecret).bind("secret");
        this.binder.bindInstanceFields(this);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}
