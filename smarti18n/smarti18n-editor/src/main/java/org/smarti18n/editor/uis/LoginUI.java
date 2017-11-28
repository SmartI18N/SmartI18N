package org.smarti18n.editor.uis;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.vaadin.components.MarginLayout;
import org.smarti18n.vaadin.utils.I18N;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

@SpringUI(path = "/login")
@Theme("smarti18n")
@Viewport("initial-scale=1, maximum-scale=1")
public class LoginUI extends UI {

    private final VaadinSharedSecurity vaadinSecurity;

    private final UserApi userApi;

    public LoginUI(final VaadinSharedSecurity vaadinSecurity, final UserApi userApi) {
        this.vaadinSecurity = vaadinSecurity;
        this.userApi = userApi;

        getPage().setTitle(I18N.translate("smarti18n.editor.title"));
    }

    @Override
    protected void init(final VaadinRequest request) {
        setSizeFull();

        final HorizontalLayout layout = new HorizontalLayout(
                createLoginForm(),
                createRegisterForm()
        );

        final VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.addComponent(layout);
        content.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        setContent(content);
    }

    private Panel createLoginForm() {
        final LoginForm loginForm = new LoginForm();
        loginForm.setSizeFull();
        loginForm.addLoginListener(event -> {
            try {
                final String username = event.getLoginParameter("username");
                final String password = event.getLoginParameter("password");

                this.vaadinSecurity.login(username, password, false);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });

        final Panel panel = new Panel();
        panel.setCaption(I18N.translate("smarti18n.editor.login.caption"));
        panel.setContent(new MarginLayout(loginForm));
        panel.setSizeUndefined();
        return panel;
    }

    private Panel createRegisterForm() {
        final LoginForm form = new LoginForm();
        form.setSizeFull();
        form.setLoginButtonCaption(I18N.translate("smarti18n.editor.register.caption"));

        form.addLoginListener(event -> {
            try {
                final String username = event.getLoginParameter("username");
                final String password = event.getLoginParameter("password");

                final User user = this.userApi.register(username, password);

                this.vaadinSecurity.login(user.getMail(), user.getPassword(), false);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });

        final Panel panel = new Panel();
        panel.setCaption(I18N.translate("smarti18n.editor.register.caption"));
        panel.setContent(new MarginLayout(form));
        panel.setSizeUndefined();
        return panel;
    }
}
