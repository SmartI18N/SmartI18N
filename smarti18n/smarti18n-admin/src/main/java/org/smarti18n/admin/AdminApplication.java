package org.smarti18n.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import org.smarti18n.api.ApiExceptionHandler;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringBootApplication(
        exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
        scanBasePackages = {"org.smarti18n.admin", "org.smarti18n.vaadin"}
)
@EnableAutoConfiguration
public class AdminApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ApiExceptionHandler());
        return restTemplate;
    }
}
