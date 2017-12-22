package org.smarti18n.editor.views;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.util.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
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

    private Locale locale1;
    private Locale locale2;

    public ProjectTranslatorView(final MessagesApi messagesApi, final ProjectsApi projectsApi) {
        super(projectsApi);

        this.messagesApi = messagesApi;
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.translator.caption"));
        setSizeFull();

        grid = new Grid<>(Message.class);
        grid.setColumns();

        grid.addComponentColumn(message1 -> new MessagePopup(locale1, message1, (translation1) -> {
            this.messagesApi.update(projectId(), message1.getKey(), locale1, translation1);

            message1.getTranslations().put(locale1, translation1);
        })).setExpandRatio(1);

        grid.addComponentColumn(message -> new MessagePopup(locale2, message, (translation) -> {
            this.messagesApi.update(projectId(), message.getKey(), locale2, translation);

            message.getTranslations().put(locale2, translation);
        })).setExpandRatio(1);

        grid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    @Override
    protected HorizontalLayout createButtonBar() {

        final IconButton reloadButton = new IconButton(
                translate("smarti18n.editor.translator.reload"),
                VaadinIcons.SHIFT_ARROW,
                clickEvent -> reloadGrid()
        );

        final IconButton languageButton = new IconButton(
                translate("smarti18n.editor.translator.update-languages"),
                VaadinIcons.FILE_ADD,
                clickEvent -> {
                    this.getUI().addWindow(new LanguageWindow(
                            project().getLocales(),
                            (locale1, locale2) -> navigateTo(VIEW_NAME, projectId(), locale1.toString(), locale2.toString())
                    ));
                });

        return new HorizontalLayout(reloadButton, languageButton);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String[] parameters = viewChangeEvent.getParameters().split("/");
        loadProjectContext(parameters[0]);

        if (parameters.length == 3) {
            this.locale1 = Locale.forLanguageTag(parameters[1]);
            this.locale2 = Locale.forLanguageTag(parameters[2]);
        } else {
            this.locale1 = Locale.ENGLISH;
            this.locale2 = Locale.GERMAN;
        }

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
