package org.smarti18n.messages.repositories;

import org.smarti18n.messages.entities.MessageEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {
}
