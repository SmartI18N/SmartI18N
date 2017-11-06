package org.smarti18n.messages.repositories;

import org.smarti18n.messages.entities.ProjectEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<ProjectEntity, String> {
}
