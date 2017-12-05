package org.smarti18n.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

}
