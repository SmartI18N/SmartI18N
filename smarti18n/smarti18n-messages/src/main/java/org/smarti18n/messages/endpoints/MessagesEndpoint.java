package org.smarti18n.messages.endpoints;

import java.util.Collection;
import java.util.Locale;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.messages.security.SecurityUtils;
import org.smarti18n.messages.service.MessagesService;

@RestController
public class MessagesEndpoint implements MessagesApi {

    private final MessagesService messagesService;

    public MessagesEndpoint(final MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ALL)
    public Collection<MessageImpl> findAll(
            @RequestParam("projectId") final String projectId) {

        return messagesService.findAll(
                SecurityUtils.getUserId(),
                projectId
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ONE)
    public MessageImpl findOne(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key) {

        return messagesService.findOne(
                SecurityUtils.getUserId(),
                projectId,
                key
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_INSERT)
    public MessageImpl insert(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key) {

        return messagesService.insert(
                SecurityUtils.getUserId(),
                projectId,
                key
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_UPDATE)
    public MessageImpl update(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key,
            @RequestParam("locale") final Locale locale,
            @RequestParam("translation") final String translation) {

        return messagesService.update(
                SecurityUtils.getUserId(),
                projectId,
                key,
                locale, translation
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_COPY)
    public MessageImpl copy(
            @RequestParam("projectId") final String projectId,
            @RequestParam("sourceKey") final String sourceKey,
            @RequestParam("targetKey") final String targetKey) {

        return messagesService.copy(
                SecurityUtils.getUserId(),
                projectId,
                sourceKey,
                targetKey
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_REMOVE)
    public void remove(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key) {

        messagesService.remove(
                SecurityUtils.getUserId(),
                projectId,
                key
        );
    }
}
