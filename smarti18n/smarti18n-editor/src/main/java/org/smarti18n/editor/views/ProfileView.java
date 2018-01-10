package org.smarti18n.editor.views;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import org.smarti18n.editor.controller.EditorController;
import org.smarti18n.models.User;
import org.smarti18n.vaadin.components.SaveButton;
import org.smarti18n.vaadin.security.SimpleUserDetails;
import org.smarti18n.vaadin.utils.I18N;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProfileView.VIEW_NAME)
public class ProfileView extends AbstractView implements View {

    public static final String VIEW_NAME = "profile";

    private final Binder<User> binder;

    public ProfileView(final EditorController editorController) {
        super(editorController);

        this.binder = new Binder<>(User.class);
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.profile.caption"));

        setSizeFull();

        addComponent(createButtonBar());

        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        final TextField textFieldMail = new TextField(I18N.translate("smarti18n.editor.profile.mail"));
        textFieldMail.setSizeFull();
        textFieldMail.setReadOnly(true);
        formLayout.addComponent(textFieldMail);

        final TextField textFieldVorname = new TextField(I18N.translate("smarti18n.editor.profile.vorname"));
        textFieldVorname.setSizeFull();
        formLayout.addComponent(textFieldVorname);

        final TextField textFieldNachname = new TextField(I18N.translate("smarti18n.editor.profile.nachname"));
        textFieldNachname.setSizeFull();
        formLayout.addComponent(textFieldNachname);

        final TextField textFieldCompany = new TextField(I18N.translate("smarti18n.editor.profile.company"));
        textFieldCompany.setSizeFull();
        formLayout.addComponent(textFieldCompany);

        final Panel panel = new Panel(formLayout);
        panel.setSizeFull();

        addComponent(panel);
        setExpandRatio(panel, 1);

        this.binder.forMemberField(textFieldMail).bind("mail");
        this.binder.forMemberField(textFieldVorname).bind("vorname");
        this.binder.forMemberField(textFieldNachname).bind("nachname");
        this.binder.forMemberField(textFieldCompany).bind("company");
        this.binder.bindInstanceFields(this);
    }

    private HorizontalLayout createButtonBar() {

        final SaveButton buttonSave = new SaveButton(
                this.editorController.clickSaveUser(this.binder, () -> navigateTo(StartView.VIEW_NAME))
        );

        return new HorizontalLayout(
                buttonSave
        );
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final SimpleUserDetails principal = (SimpleUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String username = principal.getUsername();

        this.binder.readBean(
                this.editorController.getUser(username)
        );

    }

}
