package org.smarti18n.editor.views;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TwinColSelect;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.utils.I18N;

import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectLocalesView.VIEW_NAME)
public class ProjectLocalesView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/locales";

    private static final List<Locale> AVAILABLE_LOCALES = Arrays.stream(Locale.getAvailableLocales())
            .sorted(Comparator.comparing(Locale::toString)).collect(Collectors.toList());

    private final Binder<Project> binder;

    ProjectLocalesView(final ProjectsApi projectsApi) {
        super(projectsApi);

        this.binder = new Binder<>(Project.class);
    }

    @PostConstruct
    public void init() {
        super.init(I18N.getMessage("smarti18n.editor.project-edit.locales"));

        final TwinColSelect<Locale> localeTwinColSelect = new TwinColSelect<>(
                "",
                AVAILABLE_LOCALES
        );
        localeTwinColSelect.setSizeFull();
        addComponent(localeTwinColSelect);

        this.binder.forMemberField(localeTwinColSelect).bind("locales");
        this.binder.bindInstanceFields(this);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);
    }
}
