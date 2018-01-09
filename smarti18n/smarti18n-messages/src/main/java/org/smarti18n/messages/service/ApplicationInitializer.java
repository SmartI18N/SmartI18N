package org.smarti18n.messages.service;

import java.util.Arrays;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.api.UserCredentials;
import org.smarti18n.api.UserRole;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.MessageRepository;
import org.smarti18n.messages.repositories.ProjectRepository;
import org.smarti18n.messages.repositories.UserRepository;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class ApplicationInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final String DEFAULT_PROJECT_ID = "default";
    private static final String DEFAULT_USER_MAIL = "default@smarti18n.com";

    private final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    private final SmartKeyGenerator keyGenerator;

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MessageRepository messageRepository;

    private final PasswordEncoder passwordEncoder;

    private final Environment environment;

    public ApplicationInitializer(
            final SmartKeyGenerator keyGenerator,
            final UserRepository userRepository,
            final ProjectRepository projectRepository,
            final MessageRepository messageRepository,
            final PasswordEncoder passwordEncoder,
            final Environment environment) {

        this.keyGenerator = keyGenerator;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.messageRepository = messageRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        this.logger.info("#######################################################################");
        this.logger.info("Initializing Application");

        this.projectRepository.findAll().forEach(projectEntity -> {
            projectEntity.setId(projectEntity.getId().trim().toLowerCase());
            this.projectRepository.save(projectEntity);
        });

        this.messageRepository.findAll().forEach(messageEntity -> {
            messageEntity.setKey(messageEntity.getKey().trim().toLowerCase());
            this.messageRepository.save(messageEntity);
        });

        if (!this.userRepository.findByMail(DEFAULT_USER_MAIL).isPresent()) {
            final String password = this.keyGenerator.generateKey();

            this.logger.info("Create Default User [" + DEFAULT_USER_MAIL + "] with Password [" + password + "]");

            final String encodedPassword = this.passwordEncoder.encode(password);

            final UserEntity userEntity = new UserEntity(DEFAULT_USER_MAIL, encodedPassword, UserRole.SUPERUSER);
            userEntity.setVorname("Default");
            userEntity.setNachname("Default");
            userEntity.setCompany("Default");
            this.userRepository.insert(userEntity);
        }

        final UserEntity defaultUser = this.userRepository.findByMail(DEFAULT_USER_MAIL)
                .orElseThrow(() -> new IllegalStateException("Default User [" + DEFAULT_USER_MAIL + "] doesn't exist"));

        if (!this.projectRepository.findById(DEFAULT_PROJECT_ID).isPresent()) {
            final String secret = this.keyGenerator.generateKey();

            this.logger.info("create default project [" + DEFAULT_PROJECT_ID + "] with secret [" + secret + "]");

            final ProjectEntity projectEntity = new ProjectEntity(DEFAULT_PROJECT_ID, secret, defaultUser);
            projectEntity.setName("Default Project");
            projectEntity.setDescription("Default Project");

            this.projectRepository.insert(projectEntity);
        }

        this.projectRepository.findAll().forEach(project -> {
            if (project.getOwners().isEmpty()) {
                project.addOwner(defaultUser);
                this.projectRepository.save(project);
            }
        });

        this.userRepository.findAll().stream().filter(user -> user.getRole() == null).forEach(user -> {
            user.setRole(UserRole.SUPERUSER);
            this.userRepository.save(user);
        });

        /*
         * ACTIVE PROFILE test
         */
        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {

            this.logger.info("");
            this.logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.logger.info("!!!!!!!!!!!!!!!!!!!!!! TEST INITIALIZATION !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.logger.info("");

            final String username = UserCredentials.TEST.getUsername();
            final String password = UserCredentials.TEST.getPassword();

            final String encodedPassword = this.passwordEncoder.encode(password);

            final UserEntity userEntity = new UserEntity(username, encodedPassword, UserRole.SUPERUSER);
            userEntity.setVorname("TEST");
            userEntity.setNachname("TEST");
            userEntity.setCompany("TEST");
            this.userRepository.insert(userEntity);


            final ProjectEntity projectEntity = new ProjectEntity("test", "test", defaultUser);
            projectEntity.setName("Test Project");
            projectEntity.setDescription("Test Project");

            this.projectRepository.insert(projectEntity);
        }

        this.logger.info("Initializing Application finished");
        this.logger.info("#######################################################################");
    }
}
