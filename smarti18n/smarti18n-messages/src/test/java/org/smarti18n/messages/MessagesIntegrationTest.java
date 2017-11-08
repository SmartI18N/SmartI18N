package org.smarti18n.messages;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.ApiException;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.ProjectsApiImpl;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MessagesIntegrationTest extends AbstractIntegrationTest {

    private static final String MESSAGE_KEY = "message.key";
    private static final String TRANSLATION = "ÜBERSETZUNG";
    private static final Locale LANGUAGE = Locale.GERMAN;
    private static final String SECOND_MESSAGE_KEY = "message.second-key";
    private static final String PROJECT_ID = "test";

    private MessagesApi messagesApi;

    private String projectId;
    private String projectSecret;

    @Before
    public void setUp() throws Exception {
        final ProjectsApiImpl projectsApi = new ProjectsApiImpl(new TestRestTemplate().getRestTemplate(), this.port);
        projectsApi.insert(PROJECT_ID);

        this.projectId = PROJECT_ID;
        this.projectSecret = projectsApi.generateSecret(PROJECT_ID);

        this.messagesApi = new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port);
    }

    @Test
    public void standardWorkflowMessages() throws Exception {
        assertNoMessagesFound();
        assertMessageInsert();
        assertMessageUpdate();
        assertMessageCopy();
        assertMessageDelete();
        assertSpringMessageSource();
    }

    @Test(expected = ApiException.class)
    public void existInsertMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(projectId, projectSecret, MESSAGE_KEY);
        this.messagesApi.insert(projectId, projectSecret, MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void unknownCopySource() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.copy(projectId, projectSecret, MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void existCopyMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(projectId, projectSecret, MESSAGE_KEY);
        this.messagesApi.copy(projectId, projectSecret, MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = RestClientException.class)
    public void wrongProjectId() {
        new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port).findAll("irgendwas", projectSecret);
    }

    @Test(expected = RestClientException.class)
    public void wrongProjectSecret() {
        new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port).findAll(projectId, "irgendwas");
    }

//
//    ASSERTS
//

    private void assertSpringMessageSource() {
        final Map<String, Map<Locale, String>> messages = this.messagesApi.findForSpringMessageSource(projectId, projectSecret);

        assertThat(messages.get(MESSAGE_KEY), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LANGUAGE), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LANGUAGE), is(TRANSLATION));
    }

    private void assertMessageDelete() {
        this.messagesApi.remove(projectId, projectSecret, SECOND_MESSAGE_KEY);

        final Collection<MessageImpl> messages = this.messagesApi.findAll(projectId, projectSecret);
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageCopy() {
        this.messagesApi.copy(projectId, projectSecret, MESSAGE_KEY, SECOND_MESSAGE_KEY);

        final Collection<MessageImpl> messages = this.messagesApi.findAll(projectId, projectSecret);
        assertThat(messages, hasSize(2));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
        assertThat(messages, hasItem(messageWith(SECOND_MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageUpdate() {
        this.messagesApi.update(projectId, projectSecret, MESSAGE_KEY, TRANSLATION, LANGUAGE);

        final Collection<MessageImpl> messages = this.messagesApi.findAll(projectId, projectSecret);
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageInsert() {
        this.messagesApi.insert(projectId, projectSecret, MESSAGE_KEY);

        final Collection<MessageImpl> messages = this.messagesApi.findAll(projectId, projectSecret);
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY)));
    }

    private void assertNoMessagesFound() {
        assertThat(this.messagesApi.findAll(projectId, projectSecret), is(empty()));
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