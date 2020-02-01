package org.smarti18n.messages.connectors;

import org.smarti18n.messages.messages.MessagesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
public class SpringConnectorEndpoint implements Connector {

    private final MessagesService messagesService;

    public SpringConnectorEndpoint(final MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping("/api/2/projects/{projectId}/connectors/spring")
    public Map<String, Map<Locale, String>> get(
            @PathVariable("projectId") String projectId) {

        return this.messagesService.findForSpringMessageSource(projectId);
    }
}
