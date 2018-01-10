package org.smarti18n.messages;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserApiImpl;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.User;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;
import org.smarti18n.models.UserImpl;
import org.smarti18n.models.UserSimplified;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class UserIntegrationTest extends AbstractIntegrationTest {

    private static final String NEW_USER_MAIL = "testMail";
    private static final String NEW_USER_PASSWORD = "testPassword";
    private static final String NEW_USER_VORNAME = "testVORNAME";
    private static final String NEW_USER_NACHNAME = "testNACHNAME";
    private static final String NEW_USER_COMPANY = "testCOMPANY";

    private UserApi userApi;

    @Before
    public void setUp() throws Exception {
        this.userApi = new UserApiImpl(this.restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST));
    }

    @Test
    public void standardWorkflowUsers() throws Exception {
        assertRegisterNewUser();
        assertFindUser();
        assertUpdateUser();
        assertFindAllUser();
    }

    @Test
    public void findUserWithoutLogin() throws Exception {
        final UserSimplified user = new UserApiImpl(this.restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(new UserCredentials("ignore", "ignore")))
                .findOneSimplified(UserCredentials.TEST.getUsername());

        assertThat(user, notNullValue());
    }

    @Test(expected = UserExistException.class)
    public void testRegisterUserWithExistingMail() throws Exception {
        this.userApi.register(UserCredentials.TEST.getUsername(), UserCredentials.TEST.getPassword());
    }

    @Test(expected = UserUnknownException.class)
    public void testUpdateUnknownUser() throws Exception {
        final UserImpl user = new UserImpl();
        user.setMail("unknown");
        this.userApi.update(user);
    }

    @Test(expected = UserUnknownException.class)
    public void testFindOneUnknownUser() throws Exception {
        this.userApi.findOne("unknown");
    }

    private void assertFindAllUser() {
        final List<User> users = this.userApi.findAll();

        assertThat(users, hasSize(3));
        assertThat(users, hasItem(userWith(NEW_USER_MAIL)));
    }

    private void assertUpdateUser() throws UserUnknownException {
        final User user = this.userApi.findOne(NEW_USER_MAIL);

        user.setVorname(NEW_USER_VORNAME);
        user.setNachname(NEW_USER_NACHNAME);
        user.setCompany(NEW_USER_COMPANY);

        this.userApi.update(user);

        assertThat(this.userApi.findOne(NEW_USER_MAIL), is(userWith(NEW_USER_VORNAME, NEW_USER_NACHNAME, NEW_USER_COMPANY)));
    }

    private void assertFindUser() throws UserUnknownException {
        final User user = this.userApi.findOne(NEW_USER_MAIL);

        assertThat(user, is(notNullValue()));
        assertThat(user, is(userWith(NEW_USER_MAIL)));
    }

    private void assertRegisterNewUser() throws UserExistException {
        assertThat(this.userApi.register(NEW_USER_MAIL, NEW_USER_PASSWORD), is(notNullValue()));
    }

    private TypeSafeMatcher<User> userWith(final String mail) {
        return new TypeSafeMatcher<User>() {
            @Override
            protected boolean matchesSafely(final User item) {
                return mail.equals(item.getMail());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(mail);
            }
        };
    }

    private Matcher<User> userWith(final String vorname, final String nachname, final String company) {
        return new TypeSafeMatcher<User>() {
            @Override
            protected boolean matchesSafely(final User item) {
                return vorname.equals(item.getVorname()) && nachname.equals(item.getNachname())
                        && company.equals(item.getCompany());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(vorname).appendValue(nachname).appendValue(company);
            }
        };
    }

}
