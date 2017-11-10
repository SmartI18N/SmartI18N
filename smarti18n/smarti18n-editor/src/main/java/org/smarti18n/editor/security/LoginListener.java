package org.smarti18n.editor.security;

import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserImpl;

import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;

public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final UserApi userApi;

    public LoginListener(final UserApi userApi) {
        this.userApi = userApi;
    }

    @Override
    public void onApplicationEvent(
             final InteractiveAuthenticationSuccessEvent event) {

        final Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof DefaultOAuth2User) {
            final DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            final Map<String, Object> attributes = oAuth2User.getAttributes();

            final UserImpl user = new UserImpl();
            user.setId(valueOf(attributes.get("id")));
            user.setLogin(valueOf(attributes.get("login")));
            user.setEmail(valueOf(attributes.get("email")));
            user.setName(valueOf(attributes.get("name")));
            user.setCompany(valueOf(attributes.get("company")));
            user.setType(valueOf(attributes.get("type")));
            this.userApi.logGitHubLogin(user);
        } else {
            LoggerFactory.getLogger(LoginListener.class).warn("Principal isn't a DefaultOAuth2User");
        }
    }

    private String valueOf(final Object obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }
}
