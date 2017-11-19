package org.smarti18n.editor.views;

import java.util.ArrayList;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.IconButton;
import org.smarti18n.editor.utils.ProjectContext;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectMessagesView.VIEW_NAME)
public class ProjectMessagesView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/messages";

    private final MessagesApi messagesApi;

    private Grid<Message> grid;

    public ProjectMessagesView(final MessagesApi messagesApi, final ProjectsApi projectsApi) {
        super(projectsApi);

        this.messagesApi = messagesApi;
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.message-overview.caption"));
        setSizeFull();

        grid = new Grid<>(Message.class);
        grid.setColumns("key", "localesAsString");

        grid.getColumn("key")
                .setCaption(translate("smarti18n.editor.message-overview.key"))
                .setExpandRatio(1);

        grid.getColumn("localesAsString")
                .setCaption(translate("smarti18n.editor.message-overview.locales"));

        grid.addComponentColumn(messageTranslations -> new IconButton(VaadinIcons.MINUS, clickEvent -> {
            messagesApi.remove(projectId(), messageTranslations.getKey());
            reloadGrid();
        }));

        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();
            navigateTo(ProjectMessageEditView.VIEW_NAME, projectId(), key);
        });

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);

        addComponent(createButtonBar());

    }

    private HorizontalLayout createButtonBar() {

        final IconButton newMessageButton = new IconButton(
                translate("smarti18n.editor.message-overview.add-new-message"),
                VaadinIcons.FILE_ADD,
                clickEvent -> {
                    this.getUI().addWindow(new MessageCreateWindow(this.messagesApi, projectId()));
                });

        final IconButton importMessageButton = new IconButton(
                translate("smarti18n.editor.message-overview.import-messages"),
                VaadinIcons.UPLOAD,
                clickEvent -> {
                    this.getUI().addWindow(new MessageImportWindow(this.messagesApi, project()));
                });

        final IconButton exportMessageButton = new IconButton(
                translate("smarti18n.editor.message-overview.export-messages"),
                VaadinIcons.DOWNLOAD,
                clickEvent -> {
                    this.getUI().addWindow(new MessageExportWindow(this.messagesApi, project()));
                });

        return new HorizontalLayout(newMessageButton, importMessageButton, exportMessageButton);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);

        reloadGrid();
    }

    private void reloadGrid() {
        grid.setItems(
                new ArrayList<>(this.messagesApi.findAll(projectId()))
        );
    }

    private String projectId() {
        return this.projectContext.getProject().getId();
    }

    private Project project() {
        return this.projectContext.getProject();
    }

}