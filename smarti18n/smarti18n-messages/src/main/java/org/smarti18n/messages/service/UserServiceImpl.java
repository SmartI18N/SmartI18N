package org.smarti18n.messages.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.smarti18n.api.User;
import org.smarti18n.api.UserRole;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return this.userRepository.findAll()
                .stream().collect(Collectors.toList());
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

        final boolean firstUser = this.userRepository.count() == 0;
        final UserRole role = firstUser ? UserRole.SUPERUSER : UserRole.USER;

        final String encodedPassword = this.passwordEncoder.encode(password);

        return this.userRepository.insert(new UserEntity(mail, encodedPassword, role));
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
