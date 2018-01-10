package org.smarti18n.editor.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import javax.annotation.PostConstruct;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.Message;
import org.smarti18n.models.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectTranslatorView.VIEW_NAME)
public class ProjectTranslatorView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/translator";

    private Grid<Message> grid;

    public ProjectTranslatorView(final EditorController editorController) {
        super(editorController);
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.translator.caption"));
        setSizeFull();

        this.grid = new Grid<>();

        this.grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();

            navigateTo(ProjectMessageEditView.VIEW_NAME, projectId(), key);
        });

        this.grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        this.grid.setColumnReorderingAllowed(true);
        this.grid.setSelectionMode(Grid.SelectionMode.NONE);
        this.grid.setSizeFull();
        this.grid.addStyleName("wrappable");

        addComponent(this.grid);
        setExpandRatio(this.grid, 1);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);

        reloadGrid();
    }

    private void reloadGrid() {
        final Project project = project();

        final ArrayList<Locale> locales = new ArrayList<>(project.getLocales());
        final Collection<Message> messages = this.editorController.getMessages(projectId());

        this.grid.setColumns();

        int columnNumber = 0;
        for (final Locale locale : locales) {
            this.grid.addColumn((message -> message.getTranslation(locale)))
                    .setCaption(locale.toString())
                    .setHidable(true)
                    .setHidden(columnNumber++ > 2);
        }

        this.grid.setItems(messages);
    }

    private String projectId() {
        return project().getId();
    }

    private Project project() {
        return this.projectContext.getProject();
    }

}
