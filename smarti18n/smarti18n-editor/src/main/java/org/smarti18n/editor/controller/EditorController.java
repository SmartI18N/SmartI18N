package org.smarti18n.editor.controller;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import org.smarti18n.api2.MessagesApi;
import org.smarti18n.api2.ProjectsApi;
import org.smarti18n.api2.UsersApi;
import org.smarti18n.editor.views.ProjectMessagesView;
import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;
import org.smarti18n.models.MessageCreateDTO;
import org.smarti18n.models.MessageImpl;
import org.smarti18n.models.MessageUpdateDTO;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectCreateDTO;
import org.smarti18n.models.ProjectUpdateDTO;
import org.smarti18n.models.User;
import org.smarti18n.models.UserImpl;
import org.smarti18n.models.UserUpdateDTO;
import org.smarti18n.vaadin.utils.ProjectContext;
import org.smarti18n.vaadin.utils.VaadinExceptionHandler;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static org.smarti18n.vaadin.utils.VaadinUtils.navigateTo;

/**
 * @author Marc Bellmann &lt;marc.bellmann@saxess.ag&gt;
 */
@Service
public class EditorController {

    private final UsersApi usersApi;
    private final ProjectsApi projectsApi;
    private final MessagesApi messagesApi;

    public EditorController(final UsersApi usersApi, final ProjectsApi projectsApi, final MessagesApi messagesApi) {
        this.usersApi = usersApi;
        this.projectsApi = projectsApi;
        this.messagesApi = messagesApi;
    }

    public User getUser(final String username) {
        return this.usersApi.findOne(username);
    }

    public Collection<Project> getProjects() {
        return this.projectsApi.findAll();
    }

    public Project getProject(final String projectId) {
        return this.projectsApi.findOne(projectId);
    }

    public Collection<Message> getMessages(final String projectId) {
        return this.messagesApi.findAll(projectId);
    }

    public Message getMessage(final String projectId, final String messageKey) {
        return this.messagesApi.findOne(projectId, messageKey);
    }

//    CLICK LISTENER

    public Button.ClickListener clickSaveUser(final Binder<User> binder, final ClickSuccessListener clickSuccessListener) {

        return clickEvent -> {
            try {
                final User user = new UserImpl();
                binder.writeBeanIfValid(user);
                this.usersApi.update(user.getMail(), new UserUpdateDTO(user.getVorname(), user.getNachname(), user.getCompany()));

                clickSuccessListener.success();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            }
        };

    }

    public Button.ClickListener clickAddProject(final TextField textFieldId, final ComboBox<Project> parentProjectComboBox, final ClickSuccessListener clickSuccessListener) {
        return clickEvent -> {
            try {
                final Optional<Project> parentProject = Optional.ofNullable(parentProjectComboBox.getValue());

                final Project project = this.projectsApi.create(new ProjectCreateDTO(textFieldId.getValue(), parentProject.map(Project::getId).orElse(null)));

                navigateTo(ProjectMessagesView.VIEW_NAME, project.getId());

                clickSuccessListener.success();
            } catch (ProjectExistException e) {
                VaadinExceptionHandler.handleProjectExistException();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            }
        };
    }

    public Button.ClickListener clickSaveProject(final Binder<Project> binder, final ProjectContext projectContext, final ClickSuccessListener clickSuccessListener) {
        return clickEvent -> {
            try {
                final Project project = projectContext.getProject();

                binder.writeBean(project);

                projectsApi.update(project.getId(), new ProjectUpdateDTO(project.getName(), project.getDescription(), project.getLocales()));

                clickSuccessListener.success();
            } catch (ValidationException e) {
                VaadinExceptionHandler.handleValidationException();
            } catch (UserRightsException e) {
                VaadinExceptionHandler.handleUserRightsException();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            } catch (ProjectUnknownException e) {
                VaadinExceptionHandler.handleProjectUnknownException();
            }
        };
    }

    public Button.ClickListener clickRemoveProject(final ProjectContext projectContext, final ClickSuccessListener clickSuccessListener) {
        return clickEvent -> {
            try {
                projectsApi.remove(projectContext.getProjectId());

                clickSuccessListener.success();
            } catch (ProjectUnknownException e) {
                VaadinExceptionHandler.handleProjectUnknownException();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            } catch (UserRightsException e) {
                VaadinExceptionHandler.handleUserRightsException();
            }
        };
    }

    public Button.ClickListener clickAddLocale(final String projectId, final ComboBox<Locale> localeComboBox, final ClickSuccessListener clickSuccessListener) {
        return clickEvent -> {
            try {
                final Project project = projectsApi.findOne(projectId);
                project.getLocales().add(localeComboBox.getValue());
                projectsApi.update(project.getId(), new ProjectUpdateDTO(project.getName(), project.getDescription(), project.getLocales()));

                clickSuccessListener.success();
            } catch (UserRightsException e) {
                VaadinExceptionHandler.handleUserRightsException();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            } catch (ProjectUnknownException e) {
                VaadinExceptionHandler.handleProjectUnknownException();
            }
        };
    }

    public Button.ClickListener clickRemoveLocale(final ProjectContext projectContext, final Locale locale, final ClickSuccessListener clickSuccessListener) {
        return clickEvent -> {
            try {
                final Project project = projectsApi.findOne(projectContext.getProjectId());
                project.getLocales().remove(locale);
                projectsApi.update(project.getId(), new ProjectUpdateDTO(project.getName(), project.getDescription(), project.getLocales()));

                clickSuccessListener.success();
            } catch (UserRightsException e) {
                VaadinExceptionHandler.handleUserRightsException();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            } catch (ProjectUnknownException e) {
                VaadinExceptionHandler.handleProjectUnknownException();
            }
        };
    }

    public Button.ClickListener clickInsertMessage(final String projectId, final TextField textFieldKey, final Consumer<Message> clickSuccessListener) {
        return clickEvent -> {
            try {
                final Message message = messagesApi.create(projectId, new MessageCreateDTO(textFieldKey.getValue()));

                clickSuccessListener.accept(message);
            } catch (UserRightsException e) {
                VaadinExceptionHandler.handleUserRightsException();
            } catch (ProjectUnknownException e) {
                VaadinExceptionHandler.handleProjectUnknownException();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            } catch (MessageExistException e) {
                VaadinExceptionHandler.handleMessageExistException();
            }
        };
    }

    public Button.ClickListener clickSaveTranslation(final Binder<Message> binder, final ProjectContext projectContext, final ClickSuccessListener clickSuccessListener) {

        return clickEvent -> {
            final String projectId = projectContext.getProjectId();

            final Message message = new MessageImpl();
            binder.writeBeanIfValid(message);

            saveMessage(clickSuccessListener, projectId, message);
        };
    }

    public Button.ClickListener clickSaveTranslation(final Binder<Message> binder, final String projectId, final ClickSuccessListener clickSuccessListener) {

        return clickEvent -> {
            final Message message = new MessageImpl();
            binder.writeBeanIfValid(message);

            saveMessage(clickSuccessListener, projectId, message);
        };
    }

    private void saveMessage(ClickSuccessListener clickSuccessListener, String projectId, Message message) {
        try {
            messagesApi.update(projectId, message.getKey(), new MessageUpdateDTO(message.getTranslations()));

            clickSuccessListener.success();
        } catch (UserUnknownException e) {
            VaadinExceptionHandler.handleUserUnknownException();
        } catch (MessageUnknownException e) {
            VaadinExceptionHandler.handleMessageUnknownException();
        } catch (UserRightsException e) {
            VaadinExceptionHandler.handleUserRightsException();
        } catch (ProjectUnknownException e) {
            VaadinExceptionHandler.handleProjectUnknownException();
        }
    }

    public Button.ClickListener clickRemoveMessage(final Message messageTranslations, final String projectId, final ClickSuccessListener clickSuccessListener) {
        return clickEvent -> {
            try {
                messagesApi.remove(projectId, messageTranslations.getKey());

                clickSuccessListener.success();
            } catch (UserRightsException e) {
                VaadinExceptionHandler.handleUserRightsException();
            } catch (UserUnknownException e) {
                VaadinExceptionHandler.handleUserUnknownException();
            } catch (ProjectUnknownException e) {
                VaadinExceptionHandler.handleProjectUnknownException();
            }
        };
    }

    public interface ClickSuccessListener {
        void success();
    }
}
