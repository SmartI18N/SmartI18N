package org.smarti18n.api2;

import org.smarti18n.api.AbstractApiImpl;
import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;
import org.smarti18n.models.MessageCreateDTO;
import org.smarti18n.models.MessageImpl;
import org.smarti18n.models.MessageUpdateDTO;
import org.smarti18n.models.SingleMessageUpdateDTO;
import org.smarti18n.models.UserCredentialsSupplier;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class MessagesApiImpl extends AbstractApiImpl implements MessagesApi {

    public MessagesApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, host, userCredentialsSupplier);
    }

    public MessagesApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, port, userCredentialsSupplier);
    }

    @Override
    public Collection<Message> findAll(String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        return Arrays.asList(
                get(uri(path(PATH_MESSAGES_FIND_ALL, projectId)), MessageImpl[].class)
        );
    }

    @Override
    public Message findOne(String projectId, String messageKey) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        return get(uri(path(PATH_MESSAGES_FIND_ONE, projectId, messageKey)), MessageImpl.class);
    }

    @Override
    public Message create(String projectId, MessageCreateDTO dto) throws UserRightsException, MessageExistException, UserUnknownException, ProjectUnknownException {
        return post(uri(path(PATH_MESSAGES_CREATE, projectId)), dto, MessageImpl.class);
    }

    @Override
    public Message update(String projectId, String messageKey, MessageUpdateDTO dto) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException {
        return put(uri(path(PATH_MESSAGES_UPDATE, projectId, messageKey)), dto, Message.class);
    }

    @Override
    public Message update(String projectId, String messageKey, String locale, SingleMessageUpdateDTO dto) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException {
        return put(uri(path(PATH_MESSAGES_UPDATE_SINGLE, projectId, messageKey, locale)), dto, Message.class);
    }

    @Override
    public void remove(String projectId, String messageKey) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        delete(uri(path(PATH_MESSAGES_REMOVE, projectId, messageKey)), Void.class);
    }

    @Override
    public Message copy(String projectId, String sourceMessageKey, String targetMessageKey) {
        final Message targetMessage = create(projectId, new MessageCreateDTO(targetMessageKey));
        final Message sourceMessage = findOne(projectId, sourceMessageKey);

        if (sourceMessage == null) {
            throw new MessageUnknownException();
        }

        return update(projectId, targetMessage.getKey(), new MessageUpdateDTO(sourceMessage.getTranslations()));
    }

    private static String path(String path, String projectId) {
        return path.replace("{projectId}", projectId);
    }

    private static String path(String path, String projectId, String messageKey) {
        return path(path, projectId).replace("{messageKey}", messageKey);
    }

    private static String path(String path, String projectId, String messageKey, String locale) {
        return path(path, projectId, messageKey).replace("{locale}", locale);
    }
}
