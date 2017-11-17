package org.smarti18n.messages.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public ApplicationInitializer(
            final SmartKeyGenerator keyGenerator,
            final UserRepository userRepository,
            final ProjectRepository projectRepository,
            final MessageRepository messageRepository) {

        this.keyGenerator = keyGenerator;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.messageRepository = messageRepository;
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

            final UserEntity userEntity = new UserEntity(DEFAULT_USER_MAIL, password);
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
            project.getOwners().isEmpty();
            project.addOwner(defaultUser);
            this.projectRepository.save(project);
        });

        this.logger.info("Initializing Application finished");
        this.logger.info("#######################################################################");
    }
}
