package org.smarti18n.editor.views;

import java.util.ArrayList;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

import javax.annotation.PostConstruct;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.ProjectContext;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.IconButton;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = MessageOverviewView.VIEW_NAME)
public class MessageOverviewView extends AbstractView implements View {

    public static final String VIEW_NAME = "messages/overview";

    private final MessagesApi messagesApi;

    private final ProjectContext projectContext;

    private Grid<Message> grid;

    public MessageOverviewView(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;

        this.projectContext = new ProjectContext();
    }

    @PostConstruct
    void init() {
        setCaption(translate("smarti18n.editor.message-overview.caption"));

        addComponent(createButtonBar());

        grid = new Grid<>(Message.class);
        grid.setColumns("key", "languagesAsString");

        grid.getColumn("key")
                .setCaption(translate("smarti18n.editor.message-overview.key"))
                .setExpandRatio(1);

        grid.getColumn("languagesAsString")
                .setCaption(translate("smarti18n.editor.message-overview.languages"));

        grid.addComponentColumn(messageTranslations -> new IconButton(VaadinIcons.MINUS, clickEvent -> {
            messagesApi.remove(projectId(), messageTranslations.getKey());
            Page.getCurrent().reload();
        }));

        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();
            navigator().navigateTo(MessageEditView.VIEW_NAME + "/"+ projectId() +"/" + key);
        });

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);

        setSizeFull();
    }

    private HorizontalLayout createButtonBar() {

        final Button newMessageButton = new Button(translate("smarti18n.editor.message-overview.add-new-message"), clickEvent -> {
            this.getUI().addWindow(new MessageCreateWindow(this.messagesApi, projectId()));
        });

        final Button importMessageButton = new Button(translate("smarti18n.editor.message-overview.import-messages"), clickEvent -> {
            this.getUI().addWindow(new MessageImportWindow(this.messagesApi, projectId()));
        });

        final Button exportMessageButton = new Button(translate("smarti18n.editor.message-overview.export-messages"), clickEvent -> {
            this.getUI().addWindow(new MessageExportWindow(this.messagesApi, projectId()));
        });

        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        buttonLayout.addComponents(newMessageButton, importMessageButton, exportMessageButton);

        return buttonLayout;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.projectContext.setProjectId(viewChangeEvent.getParameters());

        grid.setItems(
                new ArrayList<>(this.messagesApi.findAll(projectId()))
        );
    }

    private String projectId() {
        return this.projectContext.getProjectId();
    }

}
