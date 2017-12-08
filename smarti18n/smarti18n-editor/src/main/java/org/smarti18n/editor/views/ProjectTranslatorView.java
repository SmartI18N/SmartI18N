package org.smarti18n.editor.views;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectTranslatorView.VIEW_NAME)
public class ProjectTranslatorView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "project/translator";

    private final MessagesApi messagesApi;

    private Grid<Message> grid;

    private Locale[] locales = new Locale[]{
            Locale.GERMAN,
            Locale.ENGLISH,
            Locale.ITALIAN,
            new Locale("pt")
    };

    public ProjectTranslatorView(final MessagesApi messagesApi, final ProjectsApi projectsApi) {
        super(projectsApi);

        this.messagesApi = messagesApi;
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.message-overview.caption"));
        setSizeFull();

        grid = new Grid<>(Message.class);
        grid.setColumns();

        addMessageColumn(locales[0]);
        addMessageColumn(locales[2]);
        addMessageColumn(locales[3]);

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    private void addMessageColumn(final Locale locale) {
        grid.addComponentColumn(message -> new MessagePopup(locale, message, (translation) -> {
            this.messagesApi.update(projectId(), message.getKey(), locale, translation);
            reloadGrid();
        })).setCaption(locale.toString()).setExpandRatio(1);
    }

    @Override
    protected HorizontalLayout createButtonBar() {
        return null;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        super.enter(viewChangeEvent);

        reloadGrid();
    }

    private void reloadGrid() {
        grid.setItems(
                new ArrayList<>(this.messagesApi.findAll(projectId()))
        );
    }

    private String projectId() {
        return project().getId();
    }

    private Project project() {
        return this.projectContext.getProject();
    }

    private static class MessagePopup extends PopupView {

        private MessagePopup(final Locale locale, final Message message, final Consumer<String> consumer) {
            super(new MessageContent(locale, message, consumer));
        }
    }

    private static class MessageContent implements PopupView.Content {
        private final Locale locale;
        private final Message message;
        private final Consumer<String> consumer;

        private MessageContent(final Locale locale, final Message message, final Consumer<String> consumer) {
            this.locale = locale;
            this.message = message;
            this.consumer = consumer;
        }

        @Override
        public String getMinimizedValueAsHTML() {
            return message.getTranslation(locale);
        }

        @Override
        public Component getPopupComponent() {
            final TextArea textArea = new TextArea(
                    message.getKey(),
                    message.getTranslation(locale)
            );
            textArea.setSizeFull();
            final Button button = new Button(
                    I18N.translate("common.save"),
                    e -> consumer.accept(textArea.getValue())
            );

            final VerticalLayout verticalLayout = new VerticalLayout(
                    textArea,
                    button
            );
            return verticalLayout;
        }
    }

}
