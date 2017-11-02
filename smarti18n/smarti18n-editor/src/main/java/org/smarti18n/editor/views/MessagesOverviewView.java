package org.smarti18n.editor.views;

import java.util.Arrays;
import java.util.Collection;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.smarti18n.api.MessageTranslations;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = MessagesOverviewView.VIEW_NAME)
public class MessagesOverviewView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "messages/overview";

    private Grid<MessageTranslations> grid;

    @PostConstruct
    private void init() {
        grid = new Grid<>(MessageTranslations.class);

        grid.setColumns("key");
        grid.getColumn("key").setExpandRatio(1);

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        final TextField titleFilterField = createColumnFilterField(e -> {
            grid.setItems(getFilteredMessages(e.getValue()));
        });

        grid.appendHeaderRow().getCell("key").setComponent(titleFilterField);

        addComponent(grid);

        setSizeFull();
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
        return Arrays.asList(
                new MessageTranslations("test"),
                new MessageTranslations("test"),
                new MessageTranslations("test"),
                new MessageTranslations("test"),
                new MessageTranslations("test"),
                new MessageTranslations("test")
        );
    }
}
