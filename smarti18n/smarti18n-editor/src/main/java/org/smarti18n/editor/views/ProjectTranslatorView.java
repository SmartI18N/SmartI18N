package org.smarti18n.editor.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.ui.*;
import org.springframework.util.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

import javax.annotation.PostConstruct;

import org.smarti18n.api.Message;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.components.IconButton;
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

    public ProjectTranslatorView(final MessagesApi messagesApi, final ProjectsApi projectsApi) {
        super(projectsApi);

        this.messagesApi = messagesApi;
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.translator.caption"));
        setSizeFull();

        this.grid = new Grid<>();

        this.grid.addItemClickListener(itemClick -> {
            final String key = itemClick.getItem().getKey();
            navigateTo(ProjectMessageEditView.VIEW_NAME, projectId(), key);
        });

        this.grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        this.grid.setColumnReorderingAllowed(true);
        this.grid.setSelectionMode(Grid.SelectionMode.NONE);
        this.grid.setSizeFull();
        this.grid.addStyleName("wrappable");

        addComponent(this.grid);
        setExpandRatio(this.grid, 1);
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
        final Project project = project();

        final ArrayList<Locale> locales = new ArrayList<>(project.getLocales());
        final Collection<Message> messages = this.messagesApi.findAll(projectId());

        this.grid.setColumns();

        int columnNumber = 0;
        for (final Locale locale : locales) {
            this.grid.addColumn((message -> message.getTranslation(locale)))
                    .setCaption(locale.toString())
                    .setHidable(true)
                    .setHidden(columnNumber++ > 2);
        }

        this.grid.setItems(messages);
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
            final String translation = message.getTranslation(locale);

            return StringUtils.isEmpty(translation) ? "???" : translation;
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
            verticalLayout.setWidth(500, Unit.PIXELS);

            return verticalLayout;
        }
    }

    private static class LanguageWindow extends FormWindow {

        LanguageWindow(
                final Set<Locale> locales,
                final BiConsumer<Locale, Locale> consumer) {

            super(I18N.translate("smarti18n.editor.translator.update-languages"));

            final ComboBox<Locale> locale1Field = new ComboBox<>(
                    I18N.translate("smarti18n.editor.translator.language-one"),
                    locales
            );
            addFormComponent(
                    locale1Field
            );

            final ComboBox<Locale> locale2Field = new ComboBox<>(
                    I18N.translate("smarti18n.editor.translator.language-two"),
                    locales
            );
            addFormComponent(locale2Field);

            addFormButtons(
                    new IconButton(
                            I18N.translate("common.save"),
                            VaadinIcons.SHIFT_ARROW,
                            clickEvent -> {
                                consumer.accept(
                                        locale1Field.getValue(),
                                        locale2Field.getValue()
                                );
                                close();
                            }),
                    new CancelButton(
                            clickEvent -> close()
                    )
            );
        }
    }

}
