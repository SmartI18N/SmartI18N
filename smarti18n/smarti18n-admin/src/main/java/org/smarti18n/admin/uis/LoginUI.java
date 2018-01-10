package org.smarti18n.admin.uis;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.spring.annotation.SpringUI;
import org.smarti18n.models.UserCredentialsSupplier;
import org.smarti18n.vaadin.components.AbstractLoginUI;
import org.smarti18n.vaadin.utils.I18N;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

@SpringUI(path = "/login")
@Theme("smarti18n")
@Viewport("initial-scale=1, maximum-scale=1")
public class LoginUI extends AbstractLoginUI {

    public LoginUI(final UserCredentialsSupplier userCredentialsSupplier, final VaadinSharedSecurity vaadinSecurity) {
        super(userCredentialsSupplier, vaadinSecurity);

        getPage().setTitle(I18N.translate("smarti18n.admin.title"));
    }

}
