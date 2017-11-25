package org.smarti18n.editor.views;

import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.editor.components.LabelField;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectTranslatorView.VIEW_NAME)
public class ProjectTranslatorView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/translator";

    private final MessagesApi messagesApi;

    private ListIterator<? extends Message> messageIterator;

    private final Binder<Message> binder;

    private ComboBox<Locale> sourceLocaleComboBox;
    private ComboBox<Locale> targetLocaleComboBox;

    public ProjectTranslatorView(final MessagesApi messagesApi, final ProjectsApi projectsApi) {
        super(projectsApi);

        this.messagesApi = messagesApi;

        this.binder = new Binder<>(Message.class);
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.message-overview.caption"));
        setSizeFull();

        this.sourceLocaleComboBox = new ComboBox<>();
        this.targetLocaleComboBox = new ComboBox<>();

        final Label c = new Label("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        c.setSizeFull();

        final LabelField keyField = new LabelField();
        keyField.setSizeFull();

        final LabelField sourceField = new LabelField();
        sourceField.setSizeFull();

        final TextArea targetField = new TextArea();
        targetField.setSizeFull();

        this.binder.forField(keyField).bind("key");
        this.binder.forField(sourceField).bind(
                message -> message.getTranslation(getSourceLocale()),
                (message, translation) -> message.setTranslation(getSourceLocale(), translation)
        );
        this.binder.forField(targetField).bind(
                message -> message.getTranslation(getTargetLocale()),
                (message, translation) -> message.setTranslation(getTargetLocale(), translation)
        );
        this.binder.bindInstanceFields(this);

        final FormLayout layout = new FormLayout(
                sourceLocaleComboBox,
                targetLocaleComboBox,
                keyField,
                sourceField,
                targetField
        );
        layout.setSizeFull();
        layout.setMargin(true);

        final Panel panel = new Panel(layout);
        panel.setSizeFull();

        addComponent(panel);
        setExpandRatio(panel, 1f);
    }

    private Locale getSourceLocale() {
        return this.sourceLocaleComboBox.getValue();
    }

    private Locale getTargetLocale() {
        return this.targetLocaleComboBox.getValue();
    }

    @Override
    protected HorizontalLayout createButtonBar() {
        final Button previousButton = new Button("<");
        final Button nextButton = new Button(">");

        previousButton.addClickListener((e) -> {
            if (this.messageIterator.hasPrevious()) {
                this.binder.readBean(this.messageIterator.previous());

                nextButton.setEnabled(this.messageIterator.hasNext());
                previousButton.setEnabled(this.messageIterator.hasPrevious());
            }
        });

        nextButton.addClickListener((e) -> {
            if (this.messageIterator.hasNext()) {
                this.binder.readBean(this.messageIterator.next());

                nextButton.setEnabled(this.messageIterator.hasNext());
                previousButton.setEnabled(this.messageIterator.hasPrevious());
            }
        });

        return new HorizontalLayout(previousButton, nextButton);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);

        final Project project = project();

        this.messageIterator = new ArrayList<>(this.messagesApi.findAll(project.getId())).listIterator();

        this.sourceLocaleComboBox.setItems(project.getLocales());
        this.targetLocaleComboBox.setItems(project.getLocales());

        if (this.messageIterator.hasNext()) {
            this.binder.readBean(this.messageIterator.next());
        }
    }



    private String projectId() {
        return project().getId();
    }

    private Project project() {
        return this.projectContext.getProject();
    }

}
