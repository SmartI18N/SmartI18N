package org.smarti18n.vaadin.components;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;
import org.smarti18n.vaadin.utils.I18N;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public abstract class AbstractLoginUI extends UI{

    private final UserCredentialsSupplier userCredentialsSupplier;

    private final VaadinSharedSecurity vaadinSecurity;

    protected AbstractLoginUI(
            final UserCredentialsSupplier userCredentialsSupplier,
            final VaadinSharedSecurity vaadinSecurity) {

        this.userCredentialsSupplier = userCredentialsSupplier;
        this.vaadinSecurity = vaadinSecurity;
    }

    @Override
    protected void init(final VaadinRequest request) {
        setSizeFull();

        final HorizontalLayout layout = new HorizontalLayout(
                createLoginForm()
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

                this.userCredentialsSupplier.setUserCredentials(new UserCredentials(username, password));

                this.vaadinSecurity.login(username, password, false);
            } catch (final Exception e) {
                final UserError error = createUserError(e);
                loginForm.setComponentError(error);
            }
        });

        final Panel panel = new Panel();
        panel.setCaption(I18N.translate("smarti18n.editor.login.caption"));
        panel.setContent(new MarginLayout(loginForm));
        panel.setSizeUndefined();
        return panel;
    }

    private UserError createUserError(final Exception e) {
        if (e instanceof BadCredentialsException) {
            return new UserError(I18N.translate("smarti18n.login.error-bad-credentials"));
        }
        if (e instanceof UsernameNotFoundException) {
            return new UserError(I18N.translate("smarti18n.login.error-username-not-found"));
        }

        return new UserError(I18N.translate("smarti18n.login.error-unknown"));
    }
}
