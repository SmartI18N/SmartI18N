package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.AbstractView;
import org.smarti18n.editor.components.CancelButton;
import org.smarti18n.editor.components.SaveButton;
import org.smarti18n.editor.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectEditView.VIEW_NAME)
public class ProjectEditView extends AbstractView implements View {

    static final String VIEW_NAME = "project/edit";

    private final ProjectsApi projectsApi;

    private final Binder<Project> binder;


    private final ProjectEditGeneralTab commonTab;
    private final ProjectEditLocaleTab localeTab;
    private final ProjectEditUserTab userTab;

    protected ProjectEditView(final ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;

        this.binder = new Binder<>(Project.class);

        this.commonTab = new ProjectEditGeneralTab(
                this.binder
        );
        this.localeTab = new ProjectEditLocaleTab(
                this.binder
        );
        this.userTab = new ProjectEditUserTab(
                this.binder
        );
    }

    @PostConstruct
    private void init() {
        setCaption(translate("smarti18n.editor.project-edit.caption"));
        setSizeFull();

        addComponent(createButtonBar());

        final TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        this.commonTab.init();
        this.localeTab.init();
        this.userTab.init();

        tabSheet.addTab(this.commonTab, I18N.getMessage("smarti18n.editor.project-edit.common.tab-caption"));
        tabSheet.addTab(this.localeTab, I18N.getMessage("smarti18n.editor.project-edit.locale.tab-caption"));
        tabSheet.addTab(this.userTab, I18N.getMessage("smarti18n.editor.project-edit.user.tab-caption"));

        final Panel panel = new Panel(tabSheet);
        panel.setSizeFull();

        addComponent(panel);
        setExpandRatio(panel, 1);
    }

    private HorizontalLayout createButtonBar() {

        final SaveButton buttonSave = new SaveButton(clickEvent -> {
            final Project project = new ProjectImpl();
            binder.writeBeanIfValid(project);
            projectsApi.update(project);

            navigateTo(ProjectOverviewView.VIEW_NAME);
        });

        final CancelButton buttonCancel = new CancelButton(
                clickEvent -> navigateTo(ProjectOverviewView.VIEW_NAME)
        );

        return new HorizontalLayout(buttonSave, buttonCancel);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String projectId = viewChangeEvent.getParameters();

        final Project project = this.projectsApi.findOne(projectId);

        this.binder.readBean(project);

        this.commonTab.enter(viewChangeEvent);
        this.localeTab.enter(viewChangeEvent);
        this.userTab.enter(viewChangeEvent);
    }
}
