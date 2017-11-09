package org.smarti18n.editor.views;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.ProjectContext;
import org.smarti18n.editor.vaadin.AbstractView;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = MessageEditView.VIEW_NAME)
public class MessageEditView extends AbstractView implements View {

    static final String VIEW_NAME = "messages/edit";

    private final MessagesApi messagesApi;

    private final Binder<Message> binder;
    private final ProjectContext projectContext;

    public MessageEditView(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;

        this.binder = new Binder<>(Message.class);
        this.projectContext = new ProjectContext();
    }

    @PostConstruct
    private void init() {
        setCaption(translate("smarti18n.editor.message-edit.caption"));
        setSizeFull();

        addComponent(createButtonBar());

        final LanguageTextAreas layout = new LanguageTextAreas();
        layout.setSizeFull();

        this.binder.forField(new HiddenField()).bind("key");
        this.binder.forMemberField(layout).bind("translations");
        this.binder.bindInstanceFields(this);

        addComponent(layout);
        setExpandRatio(layout, 1f);
    }

    private HorizontalLayout createButtonBar() {

        final Button buttonSave = new Button(translate("common.save"));
        buttonSave.addClickListener(clickEvent -> {
            final Message message = new MessageImpl();
            binder.writeBeanIfValid(message);

            message.getTranslations().forEach(
                    (locale, translation) -> messagesApi.update(this.projectContext.getProjectId(), message.getKey(), translation, locale)
            );

            navigator().navigateTo(MessageOverviewView.VIEW_NAME);
        });

        final Button buttonCancle = new Button(translate("common.cancle"));
        buttonCancle.addClickListener(clickEvent -> navigator().navigateTo(MessageOverviewView.VIEW_NAME));

        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        buttonLayout.addComponents(buttonSave, buttonCancle);

        return buttonLayout;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String[] parameters = viewChangeEvent.getParameters().split("/");
        projectContext.setProjectId(parameters[0]);
        final String key = parameters[1];

        // TODO
        final Optional<? extends Message> first = this.messagesApi.findAll(this.projectContext.getProjectId()).stream()
                .filter(messageTranslations -> messageTranslations.getKey().equals(key)).findFirst();

        this.binder.readBean(first.get());
    }

    private static class LanguageTextAreas extends CustomField<Map<Locale, String>> {

        private final Layout fields = new FormLayout();
        private final Map<Locale, TextArea> textAreas = new HashMap<>();

        @Override
        protected Component initContent() {
            return fields;
        }

        @Override
        protected void doSetValue(final Map<Locale, String> value) {
            this.textAreas.clear();
            this.fields.removeAllComponents();

            for (final Map.Entry<Locale, String> entry : value.entrySet()) {
                final TextArea textArea = new TextArea(entry.getKey().getLanguage());
                textArea.setSizeFull();
                textArea.setValue(entry.getValue());

                this.textAreas.put(entry.getKey(), textArea);
                this.fields.addComponent(textArea);
            }
        }

        @Override
        public Map<Locale, String> getValue() {
            return this.textAreas.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, p -> p.getValue().getValue()));
        }
    }

    private static class HiddenField extends CustomField<String> {

        private String value;

        @Override
        protected Component initContent() {
            return null;
        }

        @Override
        protected void doSetValue(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }
}
