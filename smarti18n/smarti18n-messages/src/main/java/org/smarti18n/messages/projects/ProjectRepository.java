package org.smarti18n.messages.projects;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.smarti18n.messages.projects.ProjectEntity;
import org.smarti18n.messages.users.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface ProjectRepository extends MongoRepository<ProjectEntity, String> {

    List<ProjectEntity> findByOwners(
            UserEntity user
    );
}
