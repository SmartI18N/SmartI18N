package org.smarti18n.editor.views;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.I18N;
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

    public MessageOverviewView(final I18N i18N, final MessagesApi messagesApi) {
        super(i18N);

        this.messagesApi = messagesApi;
    }

    @PostConstruct
    void init() {
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
            messagesApi.remove(projectId, projectSecret, messageTranslations.getKey());
            enter(null);
        }));

        grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();
            navigator().navigateTo(MessageEditView.VIEW_NAME + "/" + key);
        });

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        grid.appendHeaderRow().getCell("key").setComponent(createColumnFilterField(e -> {
            grid.setItems(getFilteredMessages(e.getValue()));
        }));
        grid.appendFooterRow().getCell("key").setComponent(createAddMessageField());

        setCaption(translate("smarti18n.editor.message-overview.caption"));
        addComponent(grid);

        setSizeFull();
    }

    private Component createAddMessageField() {
        final TextField field = new TextField();
        field.setWidth("100%");
        field.addStyleName(ValoTheme.TEXTFIELD_TINY);

        final Button button = new IconButton(translate("smarti18n.editor.message-overview.add-message"), VaadinIcons.PLUS, (e -> {
            messagesApi.insert(projectId, projectSecret, field.getValue());
            enter(null);
        }));
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

    private Collection<MessageImpl> getFilteredMessages(final String filter) {
        final List<MessageImpl> all = this.messagesApi.findAll(projectId, projectSecret).stream()
                .filter(messageTranslations -> StringUtils.isEmpty(filter)  || messageTranslations.getKey().contains(filter))
                .collect(Collectors.toList());

        all.sort(Comparator.comparing(MessageImpl::getKey));
        return all;
    }

    private Navigator navigator() {
        return this.getUI().getNavigator();
    }
}
