package org.smarti18n.messages.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.UserRepository;
import org.smarti18n.models.User;
import org.smarti18n.models.UserRole;
import org.smarti18n.models.UserSimplified;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(ProjectsServiceImpl.class);

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
    public User findOne(final String mail) throws UserUnknownException {
        final Optional<UserEntity> optional = this.userRepository.findByMail(mail);
        if (!optional.isPresent()) {
            this.logger.error("User with mail [" + mail + "] doesn't exist.");

            throw new UserUnknownException();
        }

        return optional.get();
    }

    @Override
    @Transactional
    public UserSimplified findOneSimplified(final String mail) {
        return this.userRepository.findByMail(mail)
                .map(UserSimplified::new)
                .orElse(null);
    }

    @Override
    @Transactional
    public User register(final String mail, final String password) throws UserExistException {
        if (this.userRepository.findByMail(mail).isPresent()) {
            this.logger.error("User with Mail [" + mail + "] already exist!");

            throw new UserExistException();
        }

        final boolean firstUser = this.userRepository.count() == 0;
        final UserRole role = firstUser ? UserRole.SUPERUSER : UserRole.USER;

        final String encodedPassword = this.passwordEncoder.encode(password);

        return this.userRepository.insert(new UserEntity(mail, encodedPassword, role));
    }

    @Override
    @Transactional
    public User update(final User user) throws UserUnknownException {
        final Optional<UserEntity> optional = this.userRepository.findByMail(user.getMail());
        if (!optional.isPresent()) {
            this.logger.error("User with mail [" + user.getMail() + "] doesn't exist.");

            throw new UserUnknownException();
        }

        final UserEntity userEntity = optional.get();

        userEntity.setVorname(user.getVorname());
        userEntity.setNachname(user.getNachname());
        userEntity.setCompany(user.getCompany());

        return this.userRepository.save(userEntity);
    }
}
