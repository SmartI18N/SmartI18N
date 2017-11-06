package org.smarti18n.editor.views;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.I18N;
import org.smarti18n.editor.vaadin.IconButton;

import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = MessageEditView.VIEW_NAME)
public class MessageEditView extends AbstractView implements View {

    static final String VIEW_NAME = "message/edit";

    private static final List<Locale> LOCALES = Arrays.asList(
            Locale.GERMAN, Locale.ENGLISH, Locale.FRENCH, Locale.ITALIAN
    );

    private final MessagesApi messagesApi;

    public MessageEditView(final I18N i18N, final MessagesApi messagesApi) {
        super(i18N);
        this.messagesApi = messagesApi;
    }

    @PostConstruct
    private void init() {

    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String key = viewChangeEvent.getParameters();

        final Optional<MessageImpl> first = this.messagesApi.findAll().stream()
                .filter(messageTranslations -> messageTranslations.getKey().equals(key)).findFirst();

        removeAllComponents();

        first.ifPresent(messageTranslations -> {
            setCaption(messageTranslations.getKey());

            final VerticalLayout textAreas = new VerticalLayout();
            textAreas.setMargin(false);

            showTranslationArea(textAreas, messageTranslations);

            final ComboBox<Locale> select = new ComboBox<>(translate("smarti18n.editor.message-edit.add"), LOCALES);
            select.addSelectionListener((SingleSelectionListener<Locale>) event -> {
                messageTranslations.putTranslation(select.getValue());
                showTranslationArea(textAreas, messageTranslations);
            });

            final Button saveButton = new IconButton(translate("smarti18n.editor.message-edit.save"), VaadinIcons.LOCK, clickEvent -> {
                for (Map.Entry<Locale, String> entry : messageTranslations.getTranslations().entrySet()) {
                    this.messagesApi.save(messageTranslations.getKey(), entry.getValue(), entry.getKey());
                    refreshMessageSource();
                    viewChangeEvent.getNavigator().navigateTo(MessageOverviewView.VIEW_NAME);
                }
            });

            addComponent(select);
            addComponent(textAreas);
            addComponent(saveButton);
        });
    }

    private void showTranslationArea(final Layout textAreas, final MessageImpl messageImpl) {
        textAreas.removeAllComponents();
        for (Map.Entry<Locale, String> entry : messageImpl.getTranslations().entrySet()) {
            final TextArea textArea = new TextArea();
            textArea.setCaption(String.valueOf(entry.getKey()));
            textArea.setValue(entry.getValue());
            textArea.setSizeFull();
            textArea.addValueChangeListener(valueChangeEvent -> entry.setValue(valueChangeEvent.getValue()));
            textArea.setReadOnly(false);
            textAreas.addComponent(textArea);
        }
    }

}
