package org.smarti18n.messages.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.smarti18n.messages.entities.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByMail(
            String mail
    );

}
