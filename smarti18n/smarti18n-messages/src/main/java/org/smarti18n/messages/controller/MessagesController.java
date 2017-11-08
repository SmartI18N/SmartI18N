package org.smarti18n.messages.controller;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.messages.service.MessagesService;

@RestController
public class MessagesController implements MessagesApi {

    private final MessagesService messagesService;

    public MessagesController(final MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ALL)
    public Collection<MessageImpl> findAll(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret) {

        return messagesService.findAll(projectId, projectSecret);
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_SPRING)
    public Map<String, Map<Locale, String>> findForSpringMessageSource(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret) {

        return messagesService.findForSpringMessageSource(projectId, projectSecret);
    }

    @Override
    @GetMapping(PATH_MESSAGES_INSERT)
    public MessageImpl insert(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("key") final String key) {

        return messagesService.insert(projectId, projectSecret, key);
    }

    @Override
    @GetMapping(PATH_MESSAGES_UPDATE)
    public MessageImpl update(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("key") final String key,
            @RequestParam("translation") final String translation,
            @RequestParam("language") final Locale language) {

        return messagesService.update(projectId, projectSecret, key, translation, language);
    }

    @Override
    @GetMapping(PATH_MESSAGES_COPY)
    public MessageImpl copy(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("sourceKey") final String sourceKey,
            @RequestParam("targetKey") final String targetKey) {

        return messagesService.copy(projectId, projectSecret, sourceKey, targetKey);
    }

    @Override
    @GetMapping(PATH_MESSAGES_REMOVE)
    public void remove(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("key") final String key) {

        messagesService.remove(projectId, projectSecret, key);
    }
}
