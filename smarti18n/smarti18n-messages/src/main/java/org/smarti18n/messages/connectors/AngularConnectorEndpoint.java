package org.smarti18n.messages.connectors;

import org.smarti18n.messages.messages.MessagesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
public class AngularConnectorEndpoint implements Connector {

    private final MessagesService messagesService;

    public AngularConnectorEndpoint(final MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping("/api/2/projects/{projectId}/connectors/angular/locale/{locale}")
    public Map<String, String> get(
            @PathVariable("projectId") String projectId,
            @PathVariable("locale") String locale) {

        return this.messagesService.findForAngularMessageSource(projectId, Locale.forLanguageTag(locale));
    }
}
