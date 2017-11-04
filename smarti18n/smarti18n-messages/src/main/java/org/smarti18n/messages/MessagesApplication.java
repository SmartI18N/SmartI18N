package org.smarti18n.messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@SpringBootApplication
@EnableAutoConfiguration
public class MessagesApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MessagesApplication.class, args);
    }
}
