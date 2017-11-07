package org.smarti18n.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.smarti18n.messages.controller.MessagesStartedApplicationListener;
import org.smarti18n.messages.controller.ProjectContext;
import org.smarti18n.messages.controller.ProjectWebRequestInterceptor;
import org.smarti18n.messages.repositories.ProjectRepository;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class MessagesApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MessagesApplication.class, args);
    }

    @Configuration
    public static class WebMvcConfig implements WebMvcConfigurer {

        @Autowired
        private ProjectContext projectContext;

        @Override
        public void addInterceptors(final InterceptorRegistry registry) {
            registry.addWebRequestInterceptor(new ProjectWebRequestInterceptor(projectContext));
        }

        @Bean
        MessagesStartedApplicationListener messagesStartedApplicationListener(final ProjectRepository projectRepository) {
            return new MessagesStartedApplicationListener(
                    projectRepository
            );
        }
    }
}
