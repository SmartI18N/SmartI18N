package org.smarti18n.messages.repositories;

import org.smarti18n.messages.entities.GitHubUser;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GitHubUserRepository extends MongoRepository<GitHubUser, String> {
}
