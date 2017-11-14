package org.smarti18n.messages;

import java.util.Locale;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.Project;
import org.smarti18n.api.SpringMessagesApi;
import org.smarti18n.api.SpringMessagesApiImpl;
import org.smarti18n.api.UserCredentials;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SpringMessagesIntegrationTest extends AbstractIntegrationTest {

    private static final String MESSAGE_KEY = "message.key";
    private static final String TRANSLATION = "ÜBERSETZUNG";
    private static final Locale LANGUAGE = Locale.GERMAN;
    private static final String PROJECT_ID = "test";

    private SpringMessagesApi springMessagesApi;

    @Before
    public void setUp() throws Exception {
        insertTestUser();
        final Project project = insertTestProject(PROJECT_ID);

        this.springMessagesApi = new SpringMessagesApiImpl(
                new TestRestTemplate().getRestTemplate(),
                this.port,
                project.getId(),
                project.getSecret()
        );
    }

    @Test
    public void standardWorkflowSpringMessages() throws Exception {
        final MessagesApiImpl messagesApi = new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), port, () -> UserCredentials.TEST);
        messagesApi.insert(PROJECT_ID, MESSAGE_KEY);
        messagesApi.update(PROJECT_ID, MESSAGE_KEY, TRANSLATION, LANGUAGE);

        final Map<String, Map<Locale, String>> messages = this.springMessagesApi.findForSpringMessageSource();

        assertThat(messages.get(MESSAGE_KEY), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LANGUAGE), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LANGUAGE), is(TRANSLATION));
    }

    @Test(expected = RestClientException.class)
    public void wrongProjectId() {
        new SpringMessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port, "irgendwas", "irgendwas")
                .findForSpringMessageSource();
    }

    @Test(expected = RestClientException.class)
    public void wrongProjectSecret() {
        new SpringMessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port, PROJECT_ID, "irgendwas")
                .findForSpringMessageSource();
    }
}