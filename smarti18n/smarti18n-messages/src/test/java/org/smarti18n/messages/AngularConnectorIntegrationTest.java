package org.smarti18n.messages;

import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.AngularMessagesApi;
import org.smarti18n.api.AngularMessagesApiImpl;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;

import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AngularConnectorIntegrationTest extends AbstractIntegrationTest {

    private static final String MESSAGE_KEY = "message.key";
    private static final String TRANSLATION = "ÜBERSETZUNG";
    private static final Locale LOCALE = Locale.GERMAN;

    private AngularMessagesApi angularMessagesApi;

    @Before
    public void setUp() throws Exception {
        this.angularMessagesApi = new AngularMessagesApiImpl(this.restTemplate.getRestTemplate(), this.port);
    }

    @Test
    public void standardWorkflowSpringMessages() throws Exception {
        final MessagesApiImpl messagesApi = new MessagesApiImpl(this.restTemplate.getRestTemplate(), port, new UserCredentialsSupplier(UserCredentials.TEST));
        messagesApi.insert(PROJECT_ID, MESSAGE_KEY);
        messagesApi.update(PROJECT_ID, MESSAGE_KEY, LOCALE, TRANSLATION);

        final Map<String, String> messages = this.angularMessagesApi.getMessages(PROJECT_ID, LOCALE);

        assertThat(messages.get(MESSAGE_KEY), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY), is(TRANSLATION));
    }

    @Test(expected = ProjectUnknownException.class)
    public void wrongProjectId() throws Exception {
        this.angularMessagesApi.getMessages("irgendwas", LOCALE);
    }
}
