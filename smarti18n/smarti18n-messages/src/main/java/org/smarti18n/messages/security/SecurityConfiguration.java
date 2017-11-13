package org.smarti18n.messages.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;

import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.UserApi;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    static final String REALM = "SMARTI18N_MESSAGES";

    private static final String ROLE_APP = "APP";
    private static final String ROLE_USER = "USER";

    @Autowired
    public void configureGlobalSecurity(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.withDefaultPasswordEncoder().username("default").password("default").roles(ROLE_APP))
                .withUser(User.withDefaultPasswordEncoder().username("test").password("test").roles(ROLE_APP, ROLE_USER))
                .withUser(User.withDefaultPasswordEncoder().username("user").password("user").roles(ROLE_APP, ROLE_USER));
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()

                .antMatchers(
                        MessagesApi.PATH_MESSAGES_FIND_SPRING
                ).hasRole(ROLE_APP)

                .antMatchers(
                        MessagesApi.PATH_MESSAGES_FIND_ALL,
                        MessagesApi.PATH_MESSAGES_FIND_ONE,
                        MessagesApi.PATH_MESSAGES_INSERT,
                        MessagesApi.PATH_MESSAGES_UPDATE,
                        MessagesApi.PATH_MESSAGES_COPY,
                        MessagesApi.PATH_MESSAGES_REMOVE,
                        ProjectsApi.PATH_PROJECTS_FIND_ALL,
                        ProjectsApi.PATH_PROJECTS_FIND_ONE,
                        ProjectsApi.PATH_PROJECTS_INSERT,
                        ProjectsApi.PATH_PROJECTS_UPDATE,
                        ProjectsApi.PATH_PROJECTS_REMOVE,
                        UserApi.PATH_USERS_UPDATE
                ).hasRole(ROLE_USER)

                .antMatchers(
                        UserApi.PATH_USERS_FIND_ONE,
                        UserApi.PATH_USERS_REGISTER
                ).permitAll()

                .anyRequest().denyAll()

                .and().httpBasic().realmName(REALM).authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
