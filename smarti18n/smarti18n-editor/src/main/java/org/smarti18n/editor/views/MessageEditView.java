package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.editor.utils.ProjectContext;
import org.smarti18n.editor.components.AbstractView;
import org.smarti18n.editor.components.CancelButton;
import org.smarti18n.editor.components.HiddenField;
import org.smarti18n.editor.components.LanguageTextAreas;
import org.smarti18n.editor.components.SaveButton;

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

        final LanguageTextAreas languageTextAreas = new LanguageTextAreas();
        languageTextAreas.setSizeFull();

        this.binder.forField(new HiddenField()).bind("key");
        this.binder.forMemberField(languageTextAreas).bind("translations");
        this.binder.bindInstanceFields(this);

        final VerticalLayout layout = new VerticalLayout(languageTextAreas);
        layout.setSizeFull();
        layout.setMargin(true);

        final Panel panel = new Panel(layout);
        panel.setSizeFull();

        addComponent(panel);
        setExpandRatio(panel, 1f);
    }

    private HorizontalLayout createButtonBar() {

        final SaveButton buttonSave = new SaveButton(clickEvent -> {
            final Message message = new MessageImpl();
            binder.writeBeanIfValid(message);

            message.getTranslations().forEach(
                    (locale, translation) -> messagesApi.update(this.projectContext.get(), message.getKey(), translation, locale)
            );

            navigateTo(MessageOverviewView.VIEW_NAME, projectContext.get());
        });

        final CancelButton buttonCancel = new CancelButton(
                clickEvent -> navigateTo(MessageOverviewView.VIEW_NAME, projectContext.get())
        );

        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        buttonLayout.addComponents(buttonSave, buttonCancel);

        return buttonLayout;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String[] parameters = viewChangeEvent.getParameters().split("/");
        projectContext.setProjectId(parameters[0]);
        final String key = parameters[1];

        final Message message = this.messagesApi.findOne(this.projectContext.get(), key);

        this.binder.readBean(message);
    }

}
