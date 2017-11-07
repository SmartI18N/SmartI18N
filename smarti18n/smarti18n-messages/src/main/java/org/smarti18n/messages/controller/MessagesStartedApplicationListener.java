package org.smarti18n.messages.controller;

import java.util.Collections;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.ProjectRepository;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class MessagesStartedApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ProjectRepository projectRepository;

    public MessagesStartedApplicationListener(final ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (!this.projectRepository.findById("default").isPresent()) {
            final ProjectEntity projectEntity = new ProjectEntity("default");
            projectEntity.setSecrets(Collections.singleton("default"));
            projectEntity.setName("Default Project");
            projectEntity.setDescription("Default Project");

            this.projectRepository.save(projectEntity);
        }
    }
}
