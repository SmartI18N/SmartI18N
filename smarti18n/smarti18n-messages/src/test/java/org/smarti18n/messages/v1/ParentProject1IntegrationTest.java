package org.smarti18n.messages.v1;

import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.v1.AngularMessagesApi;
import org.smarti18n.api.v1.AngularMessagesApiImpl;
import org.smarti18n.api.v1.MessagesApi;
import org.smarti18n.api.v1.MessagesApiImpl;
import org.smarti18n.api.v1.ProjectsApi;
import org.smarti18n.api.v1.ProjectsApiImpl;
import org.smarti18n.api.v1.SpringMessagesApi;
import org.smarti18n.api.v1.SpringMessagesApiImpl;
import org.smarti18n.messages.AbstractIntegrationTest;
import org.smarti18n.models.Project;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;

import java.util.Locale;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ParentProject1IntegrationTest extends AbstractIntegrationTest {

    private static final String PARENT_PROJECT_ID = "parent_project_id";
    private static final String CHILD_PROJECT_ID = "child_project_id";
    private static final String MESSAGE_KEY = "message-key";
    private static final String MESSAGE_TRANSLATION = "MESSAGE_TRANSLATION";

    private MessagesApi messagesApi;
    private AngularMessagesApi angularMessagesApi;
    private SpringMessagesApi springMessagesApi;

    @Before
    public void setUp() throws Exception {
        final ProjectsApi projectsApi = new ProjectsApiImpl(this.restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST));
        this.messagesApi = new MessagesApiImpl(this.restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST));

        projectsApi.insert(PARENT_PROJECT_ID, null);
        final Project childProject = projectsApi.insert(CHILD_PROJECT_ID, PARENT_PROJECT_ID);

        this.angularMessagesApi = new AngularMessagesApiImpl(this.restTemplate.getRestTemplate(), this.port);
        this.springMessagesApi = new SpringMessagesApiImpl(this.restTemplate.getRestTemplate(), this.port, CHILD_PROJECT_ID, childProject.getSecret());
    }

    @Test
    public void messagesInParent() {
        this.messagesApi.insert(PARENT_PROJECT_ID, MESSAGE_KEY);
        this.messagesApi.update(PARENT_PROJECT_ID, MESSAGE_KEY, Locale.GERMAN, MESSAGE_TRANSLATION);

        assertThat(this.messagesApi.findOne(CHILD_PROJECT_ID, MESSAGE_KEY)).isNull();
        assertThat(this.messagesApi.findAll(CHILD_PROJECT_ID)).isEmpty();

        assertThat(angularMessagesApi.getMessages(CHILD_PROJECT_ID, Locale.GERMAN)).containsOnlyKeys(MESSAGE_KEY);
        assertThat(springMessagesApi.findForSpringMessageSource()).containsKey(MESSAGE_KEY);
    }
}
