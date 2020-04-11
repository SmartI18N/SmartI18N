package org.smarti18n.messages.messages;

import org.smarti18n.api.v2.MessagesApi;
import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.security.SecurityUtils;
import org.smarti18n.models.Message;
import org.smarti18n.models.MessageCreateDTO;
import org.smarti18n.models.MessageUpdateDTO;
import org.smarti18n.models.SingleMessageUpdateDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Locale;

@RestController
public class Messages2Endpoint implements MessagesApi {

    private final MessagesService messagesService;

    public Messages2Endpoint(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ALL)
    public Collection<Message> findAll(
            @PathVariable("projectId") String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        return messagesService.findAll(
                SecurityUtils.getUserMail(),
                projectId
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ONE)
    public Message findOne(
            @PathVariable("projectId") String projectId,
            @PathVariable("messageKey") String messageKey) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        return messagesService.findOne(
                SecurityUtils.getUserMail(),
                projectId,
                messageKey
        );
    }

    @Override
    @PostMapping(PATH_MESSAGES_CREATE)
    public Message create(
            @PathVariable("projectId") String projectId,
            @RequestBody MessageCreateDTO dto) throws UserRightsException, MessageExistException, UserUnknownException, ProjectUnknownException {

        return messagesService.insert(
                SecurityUtils.getUserMail(),
                projectId,
                dto
        );
    }

    @Override
    @PutMapping(PATH_MESSAGES_UPDATE)
    public Message update(
            @PathVariable("projectId") String projectId,
            @PathVariable("messageKey") String messageKey,
            @RequestBody MessageUpdateDTO dto) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException {

        return messagesService.update(
                SecurityUtils.getUserMail(),
                projectId,
                messageKey,
                dto
        );
    }

    @Override
    @PutMapping(PATH_MESSAGES_UPDATE_SINGLE)
    public Message update(
            @PathVariable("projectId") String projectId,
            @PathVariable("messageKey") String messageKey,
            @PathVariable("locale") String locale,
            @RequestBody SingleMessageUpdateDTO dto) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException {

        return messagesService.update(
                SecurityUtils.getUserMail(),
                projectId,
                messageKey,
                Locale.forLanguageTag(locale),
                dto.getTranslation()
        );
    }

    @Override
    @DeleteMapping(PATH_MESSAGES_REMOVE)
    public void remove(
            @PathVariable("projectId") String projectId,
            @PathVariable("messageKey") String messageKey) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        messagesService.remove(
                SecurityUtils.getUserMail(),
                projectId,
                messageKey
        );
    }

    @Override
    public Message copy(String projectId, String sourceMessageKey, String targetMessageKey) {
        throw new UnsupportedOperationException("There is no endpoint for copy message. Use create and insert!");
    }
}
