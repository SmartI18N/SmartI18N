package org.smarti18n.editor.views;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.api.MessageTranslations;
import org.smarti18n.api.MessagesApi;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = MessagesEditView.VIEW_NAME)
public class MessagesEditView extends VerticalLayout implements View {

    static final String VIEW_NAME = "messages/edit";

    private final MessagesApi messagesApi;

    private Layout layout;

    public MessagesEditView(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @PostConstruct
    private void init() {
        this.layout = new VerticalLayout();

        addComponent(layout);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String key = viewChangeEvent.getParameters();

        final Optional<MessageTranslations> first = this.messagesApi.findAll().stream()
                .filter(messageTranslations -> messageTranslations.getKey().equals(key)).findFirst();

        this.layout.removeAllComponents();

        first.ifPresent(messageTranslations -> {
            setCaption(messageTranslations.getKey());

            final Layout textAreas = new VerticalLayout();

            showTranslationArea(textAreas, messageTranslations);

            final ComboBox<Locale> select = new ComboBox<>("", Arrays.asList(Locale.GERMAN, Locale.ENGLISH, Locale.FRENCH, Locale.ITALIAN));
            final Button button = new Button("Add", clickEvent -> {
                messageTranslations.getTranslations().put(select.getValue(), "");
                showTranslationArea(textAreas, messageTranslations);
            });

            layout.addComponent(textAreas);
            layout.addComponent(new HorizontalLayout(select, button));
        });
    }

    private void showTranslationArea(final Layout textAreas, final MessageTranslations messageTranslations) {
        textAreas.removeAllComponents();
        for (Map.Entry<Locale, String> entry : messageTranslations.getTranslations().entrySet()) {
            final RichTextArea textArea = new RichTextArea();
            textArea.setCaption(String.valueOf(entry.getKey()));
            textArea.setValue(entry.getValue());
            textArea.setSizeFull();
            textArea.addValueChangeListener(valueChangeEvent -> entry.setValue(valueChangeEvent.getValue()));
            textArea.setReadOnly(false);
            textAreas.addComponent(textArea);
        }
    }

}
