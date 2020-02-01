package org.smarti18n.messages.messages;

import org.smarti18n.messages.projects.ProjectEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface MessageRepository extends MongoRepository<MessageEntity, MessageEntity.MessageId> {

    Collection<MessageEntity> findByIdProject(
            @Param("project") final ProjectEntity project
    );

}
