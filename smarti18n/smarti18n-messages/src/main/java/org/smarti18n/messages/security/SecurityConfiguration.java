package org.smarti18n.messages.security;

import org.smarti18n.api.AngularMessagesApi;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.SpringMessagesApi;
import org.smarti18n.api.UserApi;
import org.smarti18n.models.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;
import java.util.Map;

import static org.smarti18n.messages.security.ProjectPrincipal.ROLE_PROJECT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    static final String REALM = "SMARTI18N_MESSAGES";

    private static final String ROLE_SUPERUSER = UserRole.SUPERUSER.name();
    private static final String ROLE_USER = UserRole.USER.name();

    @Autowired
    private UserOrProjectPrincipalService userOrProjectPrincipalService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()

                .antMatchers(
                        SpringMessagesApi.PATH_MESSAGES_FIND_SPRING
                ).hasAuthority(ROLE_PROJECT)

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
                        UserApi.PATH_USERS_FIND_ONE,
                        UserApi.PATH_USERS_UPDATE
                ).hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER)

                .antMatchers(
                        UserApi.PATH_USERS_FIND_ALL,
                        UserApi.PATH_USERS_REGISTER
                ).hasAuthority(ROLE_SUPERUSER)

                .antMatchers(
                        UserApi.PATH_USERS_FIND_ONE_SIMPLIFIED,
                        AngularMessagesApi.PATH_MESSAGES_FIND_ANGULAR
                ).permitAll()

                .antMatchers(GET, "/api/2/projects").hasAnyAuthority(ROLE_SUPERUSER)
                .antMatchers(GET, "/api/2/projects/*").hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER, ROLE_PROJECT)
                .antMatchers(POST, "/api/2/projects").hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER)
                .antMatchers(PUT, "/api/2/projects/*").hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER)
                .antMatchers(PUT, "/api/2/projects/*").hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER)
                .antMatchers(DELETE, "/api/2/projects/*").hasAnyAuthority(ROLE_SUPERUSER)

                .antMatchers(GET, "/api/2/projects/{projectId}/messages").hasAnyAuthority(ROLE_SUPERUSER, ROLE_USER, ROLE_PROJECT)
                .antMatchers(GET, "/api/2/projects/{projectId}/messages/*").hasAnyAuthority(ROLE_SUPERUSER, ROLE_USER, ROLE_PROJECT)
                .antMatchers(POST, "/api/2/projects/{projectId}/messages").hasAnyAuthority(ROLE_SUPERUSER, ROLE_USER)
                .antMatchers(PUT, "/api/2/projects/{projectId}/messages/*").hasAnyAuthority(ROLE_SUPERUSER, ROLE_USER)
                .antMatchers(PUT, "/api/2/projects/{projectId}/messages/*/locale/*").hasAnyAuthority(ROLE_SUPERUSER, ROLE_USER)
                .antMatchers(DELETE, "/api/2/projects/{projectId}/messages/*").hasAnyAuthority(ROLE_SUPERUSER, ROLE_USER)

                .antMatchers(GET, "/api/2/users").hasAuthority(ROLE_SUPERUSER)
                .antMatchers(GET, "/api/2/users/*").hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER)
                .antMatchers(POST, "/api/2/users").hasAuthority(ROLE_SUPERUSER)
                .antMatchers(PUT, "/api/2/users/*").hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER)
                .antMatchers(GET, "/api/2/simple-users/*").hasAnyAuthority(ROLE_USER, ROLE_SUPERUSER)

                .anyRequest().denyAll()

                .and().httpBasic().realmName(REALM).authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(OPTIONS, "/**");
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
        final String idForEncode = "bcrypt";
        final Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put(null, NoOpPasswordEncoder.getInstance());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping(AngularMessagesApi.PATH_MESSAGES_FIND_ANGULAR).allowedOrigins("*");
            }
        };
    }

}
