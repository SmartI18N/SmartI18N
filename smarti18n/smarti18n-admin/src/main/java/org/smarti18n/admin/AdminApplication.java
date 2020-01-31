package org.smarti18n.admin;

import org.smarti18n.api.ApiExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringBootApplication(
        scanBasePackages = {"org.smarti18n.admin", "org.smarti18n.vaadin"}
)
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
