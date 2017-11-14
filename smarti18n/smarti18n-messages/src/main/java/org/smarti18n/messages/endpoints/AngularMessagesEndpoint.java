package org.smarti18n.messages.endpoints;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.AngularMessagesApi;
import org.smarti18n.messages.service.MessagesService;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@RestController
public class AngularMessagesEndpoint implements AngularMessagesApi {

    private final MessagesService messagesService;

    public AngularMessagesEndpoint(final MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ANGULAR)
    public Map<String, String> getMessages(
            @RequestParam("projectId") final String projectId,
            @RequestParam("locale") final Locale locale) {

        return this.messagesService.findForAngularMessageSource(projectId, locale);
    }

}
