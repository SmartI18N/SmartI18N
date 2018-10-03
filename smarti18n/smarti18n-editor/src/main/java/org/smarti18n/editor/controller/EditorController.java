package org.smarti18n.editor.controller;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.UserApi;
import org.smarti18n.editor.views.ProjectMessagesView;
import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;
import org.smarti18n.models.MessageImpl;
import org.smarti18n.models.Project;
import org.smarti18n.models.User;
import org.smarti18n.models.UserImpl;
import org.smarti18n.vaadin.utils.ProjectContext;
import org.smarti18n.vaadin.utils.VaadinExceptionHandler;

import static org.smarti18n.vaadin.utils.VaadinUtils.navigateTo;

/**
 * @author Marc Bellmann &lt;marc.bellmann@saxess.ag&gt;
 */
@Service
public class EditorController {

    private final UserApi userApi;
    private final ProjectsApi projectsApi;
    private final MessagesApi messagesApi;

    public EditorController(final UserApi userApi, final ProjectsApi projectsApi, final MessagesApi messagesApi) {
        this.userApi = userApi;
        this.projectsApi = projectsApi;
        this.messagesApi = messagesApi;
    }

    public User getUser(final String username) {
        return this.userApi.findOne(username);
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
                this.userApi.update(user);

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

                final Project project = this.projectsApi.insert(textFieldId.getValue(), parentProject.map(Project::getId).orElse(null));

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

                projectsApi.update(project);

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
                projectsApi.update(project);

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
                projectsApi.update(project);

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
                final Message message = messagesApi.insert(projectId, textFieldKey.getValue());

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
            try {
                final String projectId = projectContext.getProjectId();

                final Message message = new MessageImpl();
                binder.writeBeanIfValid(message);

                messagesApi.update(projectId, message);

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
        };
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
