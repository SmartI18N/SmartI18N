package org.smarti18n.editor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.ProjectsApiImpl;
import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserApiImpl;
import org.smarti18n.api.UserCredentials;
import org.smarti18n.api.UserCredentialsSupplier;
import org.smarti18n.api.spring.Smarti18nMessageSource;
import org.smarti18n.editor.security.SimpleUserDetails;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringBootApplication(
        scanBasePackages = "org.smarti18n.editor"
)
@EnableAutoConfiguration
@Configuration
public class EditorApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EditorApplication.class, args);
    }

    @Bean
    MessagesApi messagesApi(final Environment environment) {
        return new MessagesApiImpl(restTemplate(), environment, userCredentialsSupplier());
    }

    @Bean
    ProjectsApi projectsApi(final Environment environment) {
        return new ProjectsApiImpl(restTemplate(), environment, userCredentialsSupplier());
    }

    @Bean
    UserApi userApi(final Environment environment) {
        return new UserApiImpl(restTemplate(), environment, userCredentialsSupplier());
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    @SessionScope
    UserCredentialsSupplier userCredentialsSupplier() {
        return () -> {
            final SimpleUserDetails credentials = (SimpleUserDetails) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            return new UserCredentials(
                    credentials.getUsername(),
                    credentials.getPassword()
            );
        };
    }

    @Bean
    Smarti18nMessageSource messageSource(final Environment environment) {
        return new Smarti18nMessageSource(
                environment.getProperty("smarti18n.host", MessagesApiImpl.DEFAULT_HOST),
                "smarti18n-editor",
                "4XWNbZdb78O8fL518ltlnQn85kNNdBYRybQVm6XptEE="
        );
    }
}
