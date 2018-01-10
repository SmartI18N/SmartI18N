package org.smarti18n.messages.endpoints;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.SpringMessagesApi;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.messages.security.SecurityUtils;
import org.smarti18n.messages.service.MessagesService;

@RestController
public class SpringMessagesEndpoint implements SpringMessagesApi {

    private final MessagesService messagesService;

    public SpringMessagesEndpoint(final MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_SPRING)
    public Map<String, Map<Locale, String>> findForSpringMessageSource() throws ProjectUnknownException {
        return messagesService.findForSpringMessageSource(SecurityUtils.getProjectId());
    }
}
