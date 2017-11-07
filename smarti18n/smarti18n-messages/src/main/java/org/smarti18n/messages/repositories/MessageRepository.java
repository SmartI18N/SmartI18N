package org.smarti18n.messages.repositories;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.entities.ProjectEntity;

public interface MessageRepository extends MongoRepository<MessageEntity, MessageEntity.MessageId> {

    Collection<MessageEntity> findByIdProject(
            @Param("project") final ProjectEntity project
    );

}
