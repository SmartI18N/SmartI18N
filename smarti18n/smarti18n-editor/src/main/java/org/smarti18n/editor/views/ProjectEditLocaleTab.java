package org.smarti18n.editor.views;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TwinColSelect;
import org.smarti18n.api.Project;
import org.smarti18n.editor.components.Tab;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectEditLocaleTab extends FormLayout implements Tab {

    private static final List<Locale> AVAILABLE_LOCALES = Arrays.stream(Locale.getAvailableLocales())
            .sorted(Comparator.comparing(Locale::toString)).collect(Collectors.toList());

    private final Binder<Project> binder;

    ProjectEditLocaleTab(final Binder<Project> binder) {
        this.binder = binder;
    }

    @Override
    public void init() {
        setSizeFull();
        setMargin(true);

        final TwinColSelect<Locale> localeTwinColSelect = new TwinColSelect<>(
                I18N.getMessage("smarti18n.editor.project-edit.languages"),
                AVAILABLE_LOCALES
        );
        localeTwinColSelect.setSizeFull();
        addComponent(localeTwinColSelect);

        this.binder.forMemberField(localeTwinColSelect).bind("locales");
        this.binder.bindInstanceFields(this);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
