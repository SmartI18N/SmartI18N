package org.smarti18n.editor.views;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.smarti18n.editor.components.LabelField;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.Message;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.components.LocaleTextAreas;
import org.smarti18n.vaadin.components.SaveButton;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.vaadin.utils.VaadinUtils;

import javax.annotation.PostConstruct;

import static org.smarti18n.vaadin.utils.VaadinUtils.*;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectMessageEditWindow extends FormWindow {

    public ProjectMessageEditWindow(final EditorController editorController, final String projectId, final String messageKey) {
        super(I18N.translate("smarti18n.editor.message-edit.caption") + " " + messageKey);

        final Message message = editorController.getMessage(projectId, messageKey);

        final LocaleTextAreas localeTextAreas = new LocaleTextAreas();
        localeTextAreas.setValue(message.getTranslations());

        addFormComponent(localeTextAreas);

        final SaveButton buttonSave = new SaveButton(
                editorController.clickSaveTranslation(projectId, messageKey, localeTextAreas.getValue(), () -> navigateTo(ProjectMessagesView.VIEW_NAME, projectId))
        );

        final CancelButton buttonCancel = new CancelButton(
                clickEvent -> navigateTo(ProjectMessagesView.VIEW_NAME, projectId)
        );

        addFormButtons(buttonSave, buttonCancel);
    }

}
