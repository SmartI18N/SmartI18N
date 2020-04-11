package org.smarti18n.messages.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.projects.ProjectsService;
import org.smarti18n.models.User;
import org.smarti18n.models.UserCreateDTO;
import org.smarti18n.models.UserRole;
import org.smarti18n.models.UserSimplified;
import org.smarti18n.models.UserUpdateDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(ProjectsService.class);

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<User> findAll() {
        return new ArrayList<>(this.userRepository.findAll());
    }

    @Transactional
    public User findOne(final String mail) throws UserUnknownException {
        final Optional<UserEntity> optional = this.userRepository.findByMail(mail);
        if (!optional.isPresent()) {
            this.logger.error("User with mail [" + mail + "] doesn't exist.");

            throw new UserUnknownException();
        }

        return optional.get();
    }

    @Transactional
    public UserSimplified findOneSimplified(final String mail) {
        return this.userRepository.findByMail(mail)
                .map(UserSimplified::new)
                .orElse(null);
    }

    @Transactional
    public User register(final UserCreateDTO dto) throws UserExistException {
        if (this.userRepository.findByMail(dto.getMail()).isPresent()) {
            this.logger.error("User with Mail [" + dto.getMail() + "] already exist!");

            throw new UserExistException();
        }

        final boolean firstUser = this.userRepository.count() == 0;
        final UserRole role = firstUser ? UserRole.SUPERUSER : UserRole.USER;

        final String encodedPassword = this.passwordEncoder.encode(dto.getPassword());

        return this.userRepository.insert(new UserEntity(dto.getMail(), encodedPassword, role));
    }

    @Transactional
    public User update(final String mail, final UserUpdateDTO user) throws UserUnknownException {
        final Optional<UserEntity> optional = this.userRepository.findByMail(mail);
        if (!optional.isPresent()) {
            this.logger.error("User with mail [" + mail + "] doesn't exist.");

            throw new UserUnknownException();
        }

        final UserEntity userEntity = optional.get();

        userEntity.setFirstName(user.getVorname());
        userEntity.setLastName(user.getNachname());
        userEntity.setCompany(user.getCompany());

        return this.userRepository.save(userEntity);
    }
}
