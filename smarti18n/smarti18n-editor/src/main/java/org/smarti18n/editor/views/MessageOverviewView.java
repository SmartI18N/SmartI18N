package org.smarti18n.editor.views;

import java.util.Locale;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import javax.annotation.PostConstruct;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.IconButton;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = MessageOverviewView.VIEW_NAME)
public class MessageOverviewView extends AbstractView implements View {

    public static final String VIEW_NAME = "message/overview";

    private final MessagesApi messagesApi;

    private Grid<MessageImpl> grid;

    public MessageOverviewView(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @PostConstruct
    void init() {
        setCaption(translate("smarti18n.editor.message-overview.caption"));

        addComponent(new Button(translate("smarti18n.editor.project-overview.add-new-project"), clickEvent -> {
            this.getUI().addWindow(new MessageCreateWindow(this.messagesApi));
        }));

        grid = new Grid<>(MessageImpl.class);
        grid.setColumns("key");
        grid.getColumn("key").setExpandRatio(1);
        grid.addComponentColumn(messageTranslations -> {
            final StringBuilder builder = new StringBuilder();
            for (final Locale locale : messageTranslations.getTranslations().keySet()) {
                if (builder.length() != 0) {
                    builder.append(", ");
                }
                builder.append(locale.getLanguage());
            }
            return new Label(builder.toString());
        });
        grid.addComponentColumn(messageTranslations -> new IconButton(VaadinIcons.MINUS, clickEvent -> {
            messagesApi.remove("default", "default", messageTranslations.getKey());
            enter(null);
        }));

        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();
            navigator().navigateTo(MessageEditView.VIEW_NAME + "/" + key);
        });

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);

        setSizeFull();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        grid.setItems(this.messagesApi.findAll("default", "default"));
    }

}
