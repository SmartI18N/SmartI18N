package org.smarti18n.editor.views;

import java.util.Collection;

import org.springframework.boot.web.client.RestTemplateBuilder;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.smarti18n.api.MessageTranslations;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.endpoints.MessagesApiImpl;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = MessagesOverviewView.VIEW_NAME)
public class MessagesOverviewView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "messages/overview";

    private final MessagesApi messagesApi;

    private Grid<MessageTranslations> grid;

    public MessagesOverviewView() {
        this.messagesApi = new MessagesApiImpl(new RestTemplateBuilder().build());
    }

    @PostConstruct
    private void init() {
        grid = new Grid<>(MessageTranslations.class);

        grid.setColumns("key");
        grid.getColumn("key").setExpandRatio(1);
        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();
            getUI().getNavigator().navigateTo(MessagesEditView.VIEW_NAME + "/" + key);
        });

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        grid.appendHeaderRow().getCell("key").setComponent(createColumnFilterField(e -> {
            grid.setItems(getFilteredMessages(e.getValue()));
        }));
        grid.appendFooterRow().getCell("key").setComponent(createAddMessageField());

        setCaption("Messages");
        addComponent(grid);

        setSizeFull();
    }

    private Component createAddMessageField() {
        final TextField field = new TextField();
        field.setWidth("100%");
        field.addStyleName(ValoTheme.TEXTFIELD_TINY);

        final Button button = new Button("Add", (e -> messagesApi.insert(field.getValue())));
        button.addStyleName(ValoTheme.BUTTON_TINY);

        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(field);
        horizontalLayout.addComponent(button);

        horizontalLayout.setExpandRatio(field, 1);

        return horizontalLayout;
    }

    private static TextField createColumnFilterField(final HasValue.ValueChangeListener<String> valueChangeEvent) {
        final TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setPlaceholder("Filter");
        filter.addValueChangeListener(valueChangeEvent);
        return filter;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        grid.setItems(getFilteredMessages(null));
    }

    private Collection<MessageTranslations> getFilteredMessages(final String filter) {
        return this.messagesApi.findAll();
    }
}
