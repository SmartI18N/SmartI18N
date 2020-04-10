package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.Message;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.components.LabelField;
import org.smarti18n.vaadin.components.LocaleTextAreas;
import org.smarti18n.vaadin.components.SaveButton;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.vaadin.utils.ReloadEvent;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectMessageEditWindow extends FormWindow {

    public ProjectMessageEditWindow(final EditorController editorController, final String projectId, final String messageKey, ReloadEvent reloadEvent) {
        super(I18N.translate("smarti18n.editor.message-edit.caption") + " " + messageKey);

        final LabelField keyField = new LabelField();
        final LocaleTextAreas localeTextAreas = new LocaleTextAreas();

        final Binder<Message> binder = new Binder<>(Message.class);
        binder.forField(keyField).bind("key");
        binder.forMemberField(localeTextAreas).bind("translations");
        binder.bindInstanceFields(this);
        binder.readBean(
                editorController.getMessage(projectId, messageKey)
        );

        addFormComponent(keyField);
        addFormComponent(localeTextAreas);

        final SaveButton buttonSave = new SaveButton(
                editorController.clickSaveTranslation(binder, projectId, () -> closeAndRefresh(reloadEvent))
        );

        final CancelButton buttonCancel = new CancelButton(
                clickEvent -> closeAndRefresh(reloadEvent)
        );

        addFormButtons(buttonSave, buttonCancel);
    }

    private void closeAndRefresh(ReloadEvent reloadEvent) {
        reloadEvent.reload();
        close();
    }

}
