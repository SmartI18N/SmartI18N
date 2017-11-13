package org.smarti18n.messages;

import java.util.ArrayList;
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
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.api.Project;
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
        final Project project = projectsApi.insert(PROJECT_ID);

        this.projectId = project.getId();
        this.projectSecret = project.getSecret();

        this.messagesApi = new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port);
    }

    @Test
    public void standardWorkflowMessages() throws Exception {
        assertNoMessagesFound();
        assertMessageInsert();
        assertMessageUpdate();
        assertMessageFind();
        assertMessageCopy();
        assertMessageDelete();
        assertSpringMessageSource();
    }

    @Test(expected = ApiException.class)
    public void existInsertMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(projectId, MESSAGE_KEY);
        this.messagesApi.insert(projectId, MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void unknownCopySource() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.copy(projectId, MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void existCopyMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(projectId, MESSAGE_KEY);
        this.messagesApi.copy(projectId, MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = RestClientException.class)
    public void wrongProjectId() {
        new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port).findAll("irgendwas");
    }

    @Test(expected = ApiException.class)
    public void wrongProjectSecret() {
        new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port).findForSpringMessageSource(projectId, "irgendwas");
    }

//
//    ASSERTS
//

    private void assertSpringMessageSource() {
        final Map<String, Map<String, String>> messages = (Map) this.messagesApi.findForSpringMessageSource(projectId, projectSecret);

        assertThat(messages.get(MESSAGE_KEY), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LANGUAGE.toLanguageTag()), is(notNullValue()));
        assertThat(messages.get(MESSAGE_KEY).get(LANGUAGE.toLanguageTag()), is(TRANSLATION));
    }

    private void assertMessageDelete() {
        this.messagesApi.remove(projectId, SECOND_MESSAGE_KEY);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(projectId));
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageCopy() {
        this.messagesApi.copy(projectId, MESSAGE_KEY, SECOND_MESSAGE_KEY);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(projectId));
        assertThat(messages, hasSize(2));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
        assertThat(messages, hasItem(messageWith(SECOND_MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageFind() {
        final Message message = this.messagesApi.findOne(projectId, MESSAGE_KEY);

        assertThat(message, is(notNullValue()));
        assertThat(message, is(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageUpdate() {
        this.messagesApi.update(projectId, MESSAGE_KEY, TRANSLATION, LANGUAGE);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(projectId));
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LANGUAGE, TRANSLATION)));
    }

    private void assertMessageInsert() {
        this.messagesApi.insert(projectId, MESSAGE_KEY);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(projectId));
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY)));
    }

    private void assertNoMessagesFound() {
        assertThat(this.messagesApi.findAll(projectId), is(empty()));
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