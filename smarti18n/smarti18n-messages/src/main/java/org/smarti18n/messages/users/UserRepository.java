package org.smarti18n.messages.users;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.smarti18n.messages.users.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByMail(
            String mail
    );

}
