package org.smarti18n.messages.endpoints;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.MessageRepository;
import org.smarti18n.messages.repositories.ProjectRepository;
import org.smarti18n.messages.repositories.UserRepository;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class MessagesStartedApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final String DEFAULT_PROJECT_ID = "default";
    private static final String DEFAULT_PROJECT_SECRET = "default";
    private static final String DEFAULT_USER_MAIL = "default@smarti18n.com";
    private static final String DEFAULT_USER_PASSWORD = "default";

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MessageRepository messageRepository;

    public MessagesStartedApplicationListener(
            final UserRepository userRepository,
            final ProjectRepository projectRepository,
            final MessageRepository messageRepository) {

        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        this.projectRepository.findAll().forEach(projectEntity -> {
            projectEntity.setId(projectEntity.getId().trim().toLowerCase());
            this.projectRepository.save(projectEntity);
        });

        this.messageRepository.findAll().forEach(messageEntity -> {
            messageEntity.setKey(messageEntity.getKey().trim().toLowerCase());
            this.messageRepository.save(messageEntity);
        });

        if (!this.projectRepository.findById(DEFAULT_PROJECT_ID).isPresent()) {
            final ProjectEntity projectEntity = new ProjectEntity(DEFAULT_PROJECT_ID, DEFAULT_PROJECT_SECRET);
            projectEntity.setName("Default Project");
            projectEntity.setDescription("Default Project");

            this.projectRepository.insert(projectEntity);
        }

        if (!this.userRepository.findByMail(DEFAULT_USER_MAIL).isPresent()) {
            final UserEntity userEntity = new UserEntity(DEFAULT_USER_MAIL, DEFAULT_USER_PASSWORD);
            userEntity.setVorname("Default");
            userEntity.setNachname("Default");
            userEntity.setCompany("Default");
            this.userRepository.insert(userEntity);
        }
    }
}
