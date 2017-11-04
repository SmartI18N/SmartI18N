package org.smarti18n.messages.controller;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.MessageSimple;
import org.smarti18n.api.MessageTranslations;
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
    @GetMapping(PATH_ROOT)
    public String root() {
        return "API 1: Registrierte Messages: " + this.messageRepository.count();
    }

    @Override
    @GetMapping(PATH_FIND_ONE)
    public MessageSimple findOne(
            @RequestParam("key") final String key,
            @RequestParam("language") final Locale language) {

        final Optional<MessageEntity> optional = this.messageRepository.findById(key);
        if (optional.isPresent()) {
            final MessageEntity messageEntity = optional.get();
            return new MessageSimple(
                    messageEntity.getKey(),
                    messageEntity.getTranslation(language),
                    language
            );
        }
        return null;
    }

    @Override
    @GetMapping(PATH_FIND_ALL)
    public Collection<MessageTranslations> findAll() {
        return this.messageRepository.findAll().stream().map(messageEntity -> new MessageTranslations(
                messageEntity.getKey(),
                messageEntity.getTranslations()
        )).collect(Collectors.toList());
    }

    @Override
    @GetMapping(PATH_INSERT)
    public MessageTranslations insert(
            @RequestParam("key") final String key) {

        final MessageEntity saved = this.messageRepository.insert(new MessageEntity(key));

        return new MessageTranslations(
                saved.getKey(),
                saved.getTranslations()
        );
    }

    @Override
    @GetMapping(PATH_SAVE)
    public MessageTranslations save(
            @RequestParam("key") final String key,
            @RequestParam("translation") final String translation,
            @RequestParam("language") final Locale language) {

        final Optional<MessageEntity> optional = this.messageRepository.findById(key);
        if (optional.isPresent()) {
            final MessageEntity messageEntity = optional.orElseGet(() -> new MessageEntity(key));

            messageEntity.putTranslation(language, translation);

            final MessageEntity saved = this.messageRepository.save(messageEntity);

            return new MessageTranslations(
                    saved.getKey(),
                    saved.getTranslations()
            );
        }
        throw new IllegalStateException("Message with key [" + key + "] doesn't exist.");
    }

    @Override
    @GetMapping(PATH_REMOVE)
    public void remove(final String key) {
        this.messageRepository.deleteById(key);
    }
}
