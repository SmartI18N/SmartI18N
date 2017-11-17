package org.smarti18n.messages.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.smarti18n.api.AngularMessagesApi;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.SpringMessagesApi;
import org.smarti18n.api.UserApi;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    static final String REALM = "SMARTI18N_MESSAGES";

    @Autowired
    private UserOrProjectPrincipalService userOrProjectPrincipalService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()

                .antMatchers(
                        SpringMessagesApi.PATH_MESSAGES_FIND_SPRING
                ).hasAuthority(ProjectPrincipal.ROLE_PROJECT)

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
                ).hasAuthority(UserPrincipal.ROLE_USER)

                .antMatchers(
                        UserApi.PATH_USERS_FIND_ONE,
                        UserApi.PATH_USERS_REGISTER,
                        AngularMessagesApi.PATH_MESSAGES_FIND_ANGULAR
                ).permitAll()

                .anyRequest().denyAll()

                .and().httpBasic().realmName(REALM).authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userOrProjectPrincipalService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping(AngularMessagesApi.PATH_MESSAGES_FIND_ANGULAR).allowedOrigins("*");
                registry.addMapping(UserApi.PATH_USERS_REGISTER).allowedOrigins(
                        "https://www.smarti18n.com"
                );
            }
        };
    }

}
