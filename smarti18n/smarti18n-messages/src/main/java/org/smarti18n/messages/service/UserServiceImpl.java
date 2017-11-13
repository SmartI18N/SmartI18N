package org.smarti18n.messages.service;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.smarti18n.api.User;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User findOne(final String mail) {
        return this.userRepository.findByMail(mail)
                .orElse(null);
    }

    @Override
    @Transactional
    public User register(final String mail, final String password) {
        this.userRepository.findByMail(mail).ifPresent(user -> {
            throw new IllegalStateException("User with Mail [" + mail + "] already exist!");
        });

        return this.userRepository.insert(new UserEntity(mail, password));
    }

    @Override
    @Transactional
    public User update(final User user) {
        final Optional<UserEntity> optional = this.userRepository.findByMail(user.getMail());
        if (!optional.isPresent()) {
            throw new IllegalStateException("User with mail [" + user.getMail() + "] doesn't exist.");
        }

        final UserEntity userEntity = optional.get();

        userEntity.setVorname(user.getVorname());
        userEntity.setNachname(user.getNachname());
        userEntity.setCompany(user.getCompany());

        return this.userRepository.save(userEntity);
    }
}
