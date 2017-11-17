package org.smarti18n.messages.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;

public interface ProjectRepository extends MongoRepository<ProjectEntity, String> {

    List<ProjectEntity> findByOwners(
            UserEntity user
    );
}
