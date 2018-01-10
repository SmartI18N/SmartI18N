package org.smarti18n.editor.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.Message;
import org.smarti18n.vaadin.components.IconButton;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectMessagesView.VIEW_NAME)
public class ProjectMessagesView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/messages";

    private Grid<Message> grid;

    public ProjectMessagesView(final EditorController editorController) {
        super(editorController);
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

        grid.addComponentColumn(messageTranslations -> new IconButton(
                VaadinIcons.MINUS,
                this.editorController.clickRemoveMessage(messageTranslations, projectId(), this::reloadGrid)
        ));

        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();
            navigateTo(ProjectMessageEditView.VIEW_NAME, projectId(), key);
        });

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    @Override
    protected HorizontalLayout createButtonBar() {

        final IconButton newMessageButton = new IconButton(
                translate("smarti18n.editor.message-overview.add-new-message"),
                VaadinIcons.FILE_ADD,
                clickEvent -> getUI().addWindow(new ProjectMessageCreateWindow(this.editorController, projectId())));

        return new HorizontalLayout(newMessageButton);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);

        reloadGrid();
    }

    private void reloadGrid() {
        grid.setItems(
                this.editorController.getMessages(projectId())
        );
    }

    private String projectId() {
        return this.projectContext.getProject().getId();
    }

}
