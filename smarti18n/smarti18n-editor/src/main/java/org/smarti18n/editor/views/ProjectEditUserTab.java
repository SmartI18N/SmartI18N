package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.FormLayout;
import org.smarti18n.api.Project;
import org.smarti18n.editor.components.LabelSet;
import org.smarti18n.editor.components.Tab;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectEditUserTab extends FormLayout implements Tab {

    private final Binder<Project> binder;

    ProjectEditUserTab(final Binder<Project> binder) {
        this.binder = binder;
    }

    @Override
    public void init() {
        setSizeFull();
        setMargin(true);

        final LabelSet labelSet = new LabelSet(I18N.getMessage("smarti18n.editor.project-edit.owners"));
        labelSet.setSizeFull();
        addComponent(labelSet);

        this.binder.forMemberField(labelSet).bind("owners");
        this.binder.bindInstanceFields(this);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
