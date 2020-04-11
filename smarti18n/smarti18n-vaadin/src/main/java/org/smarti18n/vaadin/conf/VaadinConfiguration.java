package org.smarti18n.vaadin.conf;

import org.smarti18n.api.spring.Smarti18nMessageSource;
import org.smarti18n.api.v2.MessagesApi;
import org.smarti18n.api.v2.MessagesApiImpl;
import org.smarti18n.api.v2.ProjectsApi;
import org.smarti18n.api.v2.ProjectsApiImpl;
import org.smarti18n.api.v2.UsersApi;
import org.smarti18n.api.v2.UsersApiImpl;
import org.smarti18n.models.UserCredentialsSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Configuration
public class VaadinConfiguration {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    MessagesApi messagesApi(final Environment environment) {
        return new MessagesApiImpl(restTemplate, environment.getProperty("smarti18n.messages.host"), userCredentialsSupplier());
    }

    @Bean
    ProjectsApi projectsApi(final Environment environment) {
        return new ProjectsApiImpl(restTemplate, environment.getProperty("smarti18n.messages.host"), userCredentialsSupplier());
    }

    @Bean
    UsersApi usersApi(final Environment environment) {
        return new UsersApiImpl(restTemplate, environment.getProperty("smarti18n.messages.host"), userCredentialsSupplier());
    }

    @Bean
    Smarti18nMessageSource messageSource(final Environment environment) {
        return new Smarti18nMessageSource(
                environment.getProperty("smarti18n.host"),
                environment.getProperty("smarti18n.projectId"),
                environment.getProperty("smarti18n.projectSecret")
        );
    }

    @Bean
    @SessionScope
    UserCredentialsSupplier userCredentialsSupplier() {
        return new UserCredentialsSupplier();
    }

}
