package org.smarti18n.messages;

import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.SpringMessagesApi;
import org.smarti18n.api.SpringMessagesApiImpl;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;
import org.springframework.web.client.RestClientException;

import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SpringConnectorIntegrationTest extends AbstractIntegrationTest {

    private static final String MESSAGE_KEY = "message.key";
    private static final String TRANSLATION = "ÜBERSETZUNG";
    private static final Locale LOCALE = Locale.GERMAN;
    private static final String PROJECT_ID = "test";

    private SpringMessagesApi springMessagesApi;

    @Before
    public void setUp() throws Exception {
        this.springMessagesApi = new SpringMessagesApiImpl(
                this.restTemplate.getRestTemplate(),
                this.port,
                "test",
                "test"
        );
    }

    @Test
    public void standardWorkflowSpringMessages() throws Exception {
        final MessagesApiImpl messagesApi = new MessagesApiImpl(this.restTemplate.getRestTemplate(), port, new UserCredentialsSupplier(UserCredentials.TEST));
        messagesApi.insert(PROJECT_ID, MESSAGE_KEY);
        messagesApi.update(PROJECT_ID, MESSAGE_KEY, LOCALE, TRANSLATION);

        final Map<String, Map<Locale, String>> messages = this.springMessagesApi.findForSpringMessageSource();

        assertThat(messages.get(MESSAGE_KEY), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LOCALE), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LOCALE), is(TRANSLATION));
    }

    @Test(expected = RestClientException.class)
    public void wrongProjectId() throws Exception  {
        new SpringMessagesApiImpl(this.restTemplate.getRestTemplate(), this.port, "irgendwas", "irgendwas")
                .findForSpringMessageSource();
    }

    @Test(expected = RestClientException.class)
    public void wrongProjectSecret() throws Exception {
        new SpringMessagesApiImpl(this.restTemplate.getRestTemplate(), this.port, PROJECT_ID, "irgendwas")
                .findForSpringMessageSource();
    }
}
