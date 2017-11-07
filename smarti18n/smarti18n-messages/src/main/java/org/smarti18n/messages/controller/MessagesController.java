package org.smarti18n.messages.controller;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.repositories.MessageRepository;

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

        if (this.messageRepository.findById(key).isPresent()) {
            throw new IllegalStateException("Message with key [" + key + "] already exist.");
        };

        return new MessageImpl(
                this.messageRepository.insert(new MessageEntity(key))
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_UPDATE)
    public MessageImpl update(
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
            @RequestParam("sourceKey") final String sourceKey,
            @RequestParam("targetKey") final String targetKey) {

        final Optional<MessageEntity> optional = this.messageRepository.findById(sourceKey);

        if (!optional.isPresent()) {
            throw new IllegalStateException("Message with key [" + sourceKey + "] doesn't exist.");
        }

        if (this.messageRepository.findById(targetKey).isPresent()) {
            throw new IllegalStateException("Message with key [" + targetKey + "] already exist.");
        };

        final MessageEntity messageEntity = optional.get();
        messageEntity.setKey(targetKey);

        final MessageEntity saved = this.messageRepository.save(messageEntity);

        return new MessageImpl(
                saved.getKey(),
                saved.getTranslations()
        );

    }

    @Override
    @GetMapping(PATH_MESSAGES_REMOVE)
    public void remove(final String key) {
        this.messageRepository.deleteById(key);
    }
}
