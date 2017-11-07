package org.smarti18n.messages;

import java.util.Collection;
import java.util.Locale;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.ApiException;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.ProjectsApiImpl;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MessagesIntegrationTest extends AbstractIntegrationTest {

    private static final String MESSAGE_KEY = "message.key";
    private static final String TRANSLATION = "ÜBERSETZUNG";
    private static final Locale LANGUAGE = Locale.GERMAN;
    private static final String SECOND_MESSAGE_KEY = "message.second-key";
    private static final String PROJECT_ID = "test";

    private MessagesApi messagesApi;

    @Before
    public void setUp() throws Exception {
        final ProjectsApiImpl projectsApi = new ProjectsApiImpl(new TestRestTemplate().getRestTemplate(), this.port);
        projectsApi.insert(PROJECT_ID);
        final String secret = projectsApi.generateSecret(PROJECT_ID);

        this.messagesApi = new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port, secret);
    }

    @Test
    public void standardWorkflowMessages() throws Exception {
        assertNoMessagesFound();
        assertMessageInsert();
        assertMessageUpdate();
        assertMessageCopy();
        assertMessageDelete();
    }

    @Test(expected = ApiException.class)
    public void existInsertMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(MESSAGE_KEY);
        this.messagesApi.insert(MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void unknownCopySource() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.copy(MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void existCopyMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(MESSAGE_KEY);
        this.messagesApi.copy(MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = RestClientException.class)
    public void wrongSecret() {
        new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port, "irgendwas").findAll();
    }

//
//    ASSERTS
//

    private void assertMessageDelete() {
        this.messagesApi.remove(SECOND_MESSAGE_KEY);

        final Collection<MessageImpl> messages = this.messagesApi.findAll();
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageCopy() {
        this.messagesApi.copy(MESSAGE_KEY, SECOND_MESSAGE_KEY);

        final Collection<MessageImpl> messages = this.messagesApi.findAll();
        assertThat(messages, hasSize(2));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
        assertThat(messages, hasItem(messageWith(SECOND_MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageUpdate() {
        this.messagesApi.update(MESSAGE_KEY, TRANSLATION, LANGUAGE);

        final Collection<MessageImpl> messages = this.messagesApi.findAll();
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageInsert() {
        this.messagesApi.insert(MESSAGE_KEY);

        final Collection<MessageImpl> messages = this.messagesApi.findAll();
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY)));
    }

    private void assertNoMessagesFound() {
        assertThat(this.messagesApi.findAll(), is(empty()));
    }

//
//    MATCHERS
//

    private Matcher<Message> messageWith(final String messageKey) {
        return new TypeSafeMatcher<Message>() {
            @Override
            protected boolean matchesSafely(final Message item) {
                return item.getKey().equals(messageKey);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(messageKey);
            }

        };
    }

    private Matcher<Message> messageWith(final String messageKey, final Locale language, final String translation) {
        return new TypeSafeMatcher<Message>() {
            @Override
            protected boolean matchesSafely(final Message item) {
                return item.getKey().equals(messageKey)
                        && item.getTranslations().containsKey(language)
                        && item.getTranslations().get(language).equals(translation);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(messageKey).appendValue(language).appendValue(translation);
            }
        };
    }

}