package org.smarti18n.messages.messages;

import org.smarti18n.api.v1.MessagesApi;
import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.security.SecurityUtils;
import org.smarti18n.models.Message;
import org.smarti18n.models.MessageCreateDTO;
import org.smarti18n.models.MessageUpdateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Locale;

@RestController
public class MessagesEndpoint implements MessagesApi {

    private final MessagesService messagesService;

    public MessagesEndpoint(final MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ALL)
    public Collection<Message> findAll(
            @RequestParam("projectId") final String projectId
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        return messagesService.findAll(
                SecurityUtils.getUserMail(),
                projectId
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ONE)
    public Message findOne(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        return messagesService.findOne(
                SecurityUtils.getUserMail(),
                projectId,
                key
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_INSERT)
    public Message insert(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key
    ) throws UserRightsException, MessageExistException, UserUnknownException, ProjectUnknownException {

        return messagesService.insert(
                SecurityUtils.getUserMail(),
                projectId,
                new MessageCreateDTO(key)
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_UPDATE)
    public Message update(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key,
            @RequestParam("locale") final Locale locale,
            @RequestParam("translation") final String translation
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        return messagesService.update(
                SecurityUtils.getUserMail(),
                projectId,
                key,
                locale, translation
        );
    }

    @Override
    @PostMapping(PATH_MESSAGES_UPDATE)
    public Message update(
            @RequestParam("projectId") final String projectId,
            @RequestBody final Message message
    ) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException {

        return messagesService.update(
                SecurityUtils.getUserMail(),
                projectId,
                message.getKey(),
                new MessageUpdateDTO(message.getTranslations())
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_COPY)
    public Message copy(
            @RequestParam("projectId") final String projectId,
            @RequestParam("sourceKey") final String sourceKey,
            @RequestParam("targetKey") final String targetKey
    ) throws UserRightsException, MessageExistException, MessageUnknownException, UserUnknownException, ProjectUnknownException {

        return messagesService.copy(
                SecurityUtils.getUserMail(),
                projectId,
                sourceKey,
                targetKey
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_REMOVE)
    public void remove(
            @RequestParam("projectId") final String projectId,
            @RequestParam("key") final String key
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        messagesService.remove(
                SecurityUtils.getUserMail(),
                projectId,
                key
        );
    }
}
