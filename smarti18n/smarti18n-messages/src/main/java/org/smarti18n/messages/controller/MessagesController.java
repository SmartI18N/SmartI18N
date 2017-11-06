package org.smarti18n.messages.controller;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.repositories.MessageRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MessagesController implements MessagesApi {

    private final MessageRepository messageRepository;

    public MessagesController(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ALL)
    public Collection<MessageImpl> findAll() {
        return this.messageRepository.findAll().stream().map(messageEntity -> new MessageImpl(
                messageEntity.getKey(),
                messageEntity.getTranslations()
        )).collect(Collectors.toList());
    }

    @Override
    @GetMapping(PATH_MESSAGES_INSERT)
    public MessageImpl insert(
            @RequestParam("key") final String key) {

        return new MessageImpl(
                this.messageRepository.insert(new MessageEntity(key))
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_SAVE)
    public MessageImpl save(
            @RequestParam("key") final String key,
            @RequestParam("translation") final String translation,
            @RequestParam("language") final Locale language) {

        final Optional<MessageEntity> optional = this.messageRepository.findById(key);
        final MessageEntity messageEntity = optional.orElseGet(() -> new MessageEntity(key));

        messageEntity.putTranslation(language, translation);

        final MessageEntity saved = this.messageRepository.save(messageEntity);

        return new MessageImpl(
                saved.getKey(),
                saved.getTranslations()
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_COPY)
    public MessageImpl copy(
            @RequestParam("key") final String sourceKey,
            @RequestParam("key") final String targetKey) {

        final Optional<MessageEntity> optional = this.messageRepository.findById(sourceKey);
        if (optional.isPresent()) {
            final MessageEntity messageEntity = optional.get();
            messageEntity.setKey(targetKey);

            final MessageEntity saved = this.messageRepository.save(messageEntity);

            return new MessageImpl(
                    saved.getKey(),
                    saved.getTranslations()
            );
        }
        return null;
    }

    @Override
    @GetMapping(PATH_MESSAGES_REMOVE)
    public void remove(final String key) {
        this.messageRepository.deleteById(key);
    }
}
