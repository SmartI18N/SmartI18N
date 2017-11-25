package org.smarti18n.editor.views;

import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.LabelField;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.LocaleTextAreas;
import org.smarti18n.vaadin.components.SaveButton;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectMessageEditView.VIEW_NAME)
public class ProjectMessageEditView extends AbstractProjectView implements View {

    static final String VIEW_NAME = "messages/edit";

    private final MessagesApi messagesApi;

    private final Binder<Message> binder;

    public ProjectMessageEditView(final MessagesApi messagesApi, final ProjectsApi projectApi) {
        super(projectApi);

        this.messagesApi = messagesApi;

        this.binder = new Binder<>(Message.class);
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.message-edit.caption"));

        setSizeFull();

        final LabelField keyField = new LabelField();
        final LocaleTextAreas localeTextAreas = new LocaleTextAreas();

        this.binder.forField(keyField).bind("key");
        this.binder.forMemberField(localeTextAreas).bind("translations");
        this.binder.bindInstanceFields(this);

        final VerticalLayout layout = new VerticalLayout(keyField, localeTextAreas);
        layout.setMargin(true);

        final Panel panel = new Panel(layout);

        addComponent(panel);
        setExpandRatio(panel, 1f);
    }

    @Override
    protected HorizontalLayout createButtonBar() {

        final SaveButton buttonSave = new SaveButton(clickEvent -> {
            final Message message = new MessageImpl();
            binder.writeBeanIfValid(message);

            message.getTranslations().forEach(
                    (locale, translation) -> messagesApi.update(projectId(), message.getKey(), locale, translation)
            );

            navigateTo(ProjectMessagesView.VIEW_NAME, projectId());
        });

        final CancelButton buttonCancel = new CancelButton(
                clickEvent -> navigateTo(ProjectMessagesView.VIEW_NAME, projectId())
        );

        return new HorizontalLayout(buttonSave, buttonCancel);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String[] parameters = viewChangeEvent.getParameters().split("/");
        loadProjectContext(parameters[0]);
        final String key = parameters[1];

        final Message message = this.messagesApi.findOne(projectId(), key);

        this.binder.readBean(message);
    }

    private String projectId() {
        return this.projectContext.getProjectId();
    }

}
