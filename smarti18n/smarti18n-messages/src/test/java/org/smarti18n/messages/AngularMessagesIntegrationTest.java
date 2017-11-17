package org.smarti18n.messages;

import java.util.Locale;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.AngularMessagesApi;
import org.smarti18n.api.AngularMessagesApiImpl;
import org.smarti18n.api.ApiException;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.UserCredentials;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AngularMessagesIntegrationTest extends AbstractIntegrationTest {

    private static final String MESSAGE_KEY = "message.key";
    private static final String TRANSLATION = "ÜBERSETZUNG";
    private static final Locale LOCALE = Locale.GERMAN;
    private static final String PROJECT_ID = "test";

    private AngularMessagesApi angularMessagesApi;

    @Before
    public void setUp() throws Exception {
        insertTestUser(UserCredentials.TEST.getUsername(), UserCredentials.TEST.getPassword());
        insertTestProject(PROJECT_ID);

        this.angularMessagesApi = new AngularMessagesApiImpl(
                new TestRestTemplate().getRestTemplate(),
                this.port
        );
    }

    @Test
    public void standardWorkflowSpringMessages() throws Exception {
        final MessagesApiImpl messagesApi = new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), port, () -> UserCredentials.TEST);
        messagesApi.insert(PROJECT_ID, MESSAGE_KEY);
        messagesApi.update(PROJECT_ID, MESSAGE_KEY, LOCALE, TRANSLATION);

        final Map<String, String> messages = this.angularMessagesApi.getMessages(PROJECT_ID, LOCALE);

        assertThat(messages.get(MESSAGE_KEY), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY), is(TRANSLATION));
    }

    @Test(expected = ApiException.class)
    public void wrongProjectId() {
        this.angularMessagesApi.getMessages("irgendwas", LOCALE);
    }
}