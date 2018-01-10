package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.smarti18n.editor.components.LabelField;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.Message;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.LocaleTextAreas;
import org.smarti18n.vaadin.components.SaveButton;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProjectMessageEditView.VIEW_NAME)
public class ProjectMessageEditView extends AbstractProjectView implements View {

    public static final String VIEW_NAME = "messages/edit";

    private final Binder<Message> binder;

    public ProjectMessageEditView(final EditorController editorController) {
        super(editorController);

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
        final SaveButton buttonSave = new SaveButton(
                this.editorController.clickSaveTranslation(binder, projectContext, () -> navigateTo(ProjectMessagesView.VIEW_NAME, projectId()))
        );

        final CancelButton buttonCancel = new CancelButton(
                clickEvent -> navigateTo(ProjectMessagesView.VIEW_NAME, projectId())
        );

        return new HorizontalLayout(buttonSave, buttonCancel);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final String[] parameters = viewChangeEvent.getParameters().split("/");

        this.projectContext.setProject(
                this.editorController.getProject(parameters[0])
        );
        this.binder.readBean(
                this.editorController.getMessage(projectId(), parameters[1])
        );
    }

    private String projectId() {
        return this.projectContext.getProjectId();
    }

}
