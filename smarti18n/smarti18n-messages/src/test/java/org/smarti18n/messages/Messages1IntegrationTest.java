package org.smarti18n.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.MessagesApiImpl;
import org.smarti18n.exceptions.ApiException;
import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Messages1IntegrationTest extends AbstractIntegrationTest {

    private static final String MESSAGE_KEY = "message.key";
    private static final String TRANSLATION = "ÜBERSETZUNG";
    private static final Locale LOCALE = Locale.GERMAN;
    private static final String SECOND_MESSAGE_KEY = "message.second-key";

    private MessagesApi messagesApi;

    @Before
    public void setUp() throws Exception {
        this.messagesApi = new MessagesApiImpl(this.restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST));
    }

    @Test
    public void standardWorkflowMessages() throws Exception {
        assertNoMessagesFound();
        assertMessageInsert();
        assertMessageUpdate();
        assertMessageFind();
        assertMessageCopy();
        assertMessageDelete();
    }

    @Test(expected = ApiException.class)
    public void existInsertMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(PROJECT_ID, MESSAGE_KEY);
        this.messagesApi.insert(PROJECT_ID, MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void unknownCopySource() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.copy(PROJECT_ID, MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = ApiException.class)
    public void existCopyMessages() throws Exception {
        assertNoMessagesFound();

        this.messagesApi.insert(PROJECT_ID, MESSAGE_KEY);
        this.messagesApi.copy(PROJECT_ID, MESSAGE_KEY, MESSAGE_KEY);
    }

    @Test(expected = ProjectUnknownException.class)
    public void wrongProjectId() {
        new MessagesApiImpl(this.restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST)).findAll("irgendwas");
    }

//
//    ASSERTS
//

    private void assertMessageDelete() throws ProjectUnknownException, UserUnknownException, UserRightsException {
        this.messagesApi.remove(PROJECT_ID, SECOND_MESSAGE_KEY);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(PROJECT_ID));
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LOCALE, TRANSLATION)));
    }

    private void assertMessageCopy() throws UserRightsException, MessageExistException, UserUnknownException, MessageUnknownException, ProjectUnknownException {
        this.messagesApi.copy(PROJECT_ID, MESSAGE_KEY, SECOND_MESSAGE_KEY);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(PROJECT_ID));
        assertThat(messages, hasSize(2));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LOCALE, TRANSLATION)));
        assertThat(messages, hasItem(messageWith(SECOND_MESSAGE_KEY, LOCALE, TRANSLATION)));
    }

    private void assertMessageFind() throws ProjectUnknownException, UserUnknownException, UserRightsException {
        final Message message = this.messagesApi.findOne(PROJECT_ID, MESSAGE_KEY);

        assertThat(message, is(notNullValue()));
        assertThat(message, is(messageWith(MESSAGE_KEY, LOCALE, TRANSLATION)));
    }

    private void assertMessageUpdate() throws ProjectUnknownException, UserUnknownException, UserRightsException, MessageUnknownException {
        this.messagesApi.update(PROJECT_ID, MESSAGE_KEY, LOCALE, TRANSLATION);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(PROJECT_ID));
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY, LOCALE, TRANSLATION)));

        this.messagesApi.update(PROJECT_ID, messages.stream().findFirst().get());
    }

    private void assertMessageInsert() throws UserRightsException, MessageExistException, UserUnknownException, ProjectUnknownException {
        this.messagesApi.insert(PROJECT_ID, MESSAGE_KEY);

        final Collection<Message> messages = new ArrayList<>(this.messagesApi.findAll(PROJECT_ID));
        assertThat(messages, hasSize(1));
        assertThat(messages, hasItem(messageWith(MESSAGE_KEY)));
    }

    private void assertNoMessagesFound() throws ProjectUnknownException, UserUnknownException, UserRightsException {
        assertThat(this.messagesApi.findAll(PROJECT_ID), is(empty()));
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

    private Matcher<Message> messageWith(final String messageKey, final Locale locale, final String translation) {
        return new TypeSafeMatcher<Message>() {
            @Override
            protected boolean matchesSafely(final Message item) {
                return item.getKey().equals(messageKey)
                        && item.getTranslations().containsKey(locale)
                        && item.getTranslations().get(locale).equals(translation);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(messageKey).appendValue(locale).appendValue(translation);
            }
        };
    }

}
