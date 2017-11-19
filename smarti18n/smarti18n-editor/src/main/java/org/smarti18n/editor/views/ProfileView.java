package org.smarti18n.editor.views;

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

import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserImpl;
import org.smarti18n.editor.components.SaveButton;
import org.smarti18n.editor.security.SimpleUserDetails;
import org.smarti18n.editor.utils.I18N;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProfileView.VIEW_NAME)
public class ProfileView extends AbstractView implements View {

    public static final String VIEW_NAME = "profile";

    private final UserApi userApi;

    private final Binder<User> binder;

    public ProfileView(final UserApi userApi) {
        this.userApi = userApi;
        this.binder = new Binder<>(User.class);
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.profile.caption"));

        setSizeFull();

        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        final TextField textFieldMail = new TextField(I18N.getMessage("smarti18n.editor.profile.mail"));
        textFieldMail.setSizeFull();
        textFieldMail.setReadOnly(true);
        formLayout.addComponent(textFieldMail);

        final TextField textFieldVorname = new TextField(I18N.getMessage("smarti18n.editor.profile.vorname"));
        textFieldVorname.setSizeFull();
        formLayout.addComponent(textFieldVorname);

        final TextField textFieldNachname = new TextField(I18N.getMessage("smarti18n.editor.profile.nachname"));
        textFieldNachname.setSizeFull();
        formLayout.addComponent(textFieldNachname);

        final TextField textFieldCompany = new TextField(I18N.getMessage("smarti18n.editor.profile.company"));
        textFieldCompany.setSizeFull();
        formLayout.addComponent(textFieldCompany);

        final Panel panel = new Panel(formLayout);
        panel.setSizeFull();

        addComponent(panel);
        setExpandRatio(panel, 1);

        addComponent(createButtonBar());

        this.binder.forMemberField(textFieldMail).bind("mail");
        this.binder.forMemberField(textFieldVorname).bind("vorname");
        this.binder.forMemberField(textFieldNachname).bind("nachname");
        this.binder.forMemberField(textFieldCompany).bind("company");
        this.binder.bindInstanceFields(this);
    }

    private HorizontalLayout createButtonBar() {

        final SaveButton buttonSave = new SaveButton(clickEvent -> {
            final User user = new UserImpl();
            binder.writeBeanIfValid(user);
            this.userApi.update(user);

            navigateTo(StartView.VIEW_NAME);
        });

        return new HorizontalLayout(
                buttonSave
        );
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final SimpleUserDetails principal = (SimpleUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final User user = this.userApi.findOne(
                principal.getUsername()
        );

        this.binder.readBean(user);

    }

}
