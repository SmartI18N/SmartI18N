package org.smarti18n.messages;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserApiImpl;
import org.smarti18n.api.UserCredentials;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class UserIntegrationTest extends AbstractIntegrationTest {

    private static final String MAIL = UserCredentials.TEST.getUsername();
    private static final String PASSWORD = UserCredentials.TEST.getPassword();
    private static final String VORNAME = "testVORNAME";
    private static final String NACHNAME = "testNACHNAME";
    private static final String COMPANY = "testCOMPANY";

    private UserApi userApi;

    @Before
    public void setUp() throws Exception {
        this.userApi = new UserApiImpl(new TestRestTemplate().getRestTemplate(), this.port, () -> UserCredentials.TEST);
    }

    @Test
    public void standardWorkflowUsers() throws Exception {
        assertRegisterNewUser();
        assertFindUser();
        assertUpdateUser();
    }

    private void assertUpdateUser() {
        final User user = this.userApi.findOne(MAIL);

        user.setVorname(VORNAME);
        user.setNachname(NACHNAME);
        user.setCompany(COMPANY);

        this.userApi.update(user);

        assertThat(this.userApi.findOne(MAIL), is(userWith(VORNAME, NACHNAME, COMPANY)));
    }

    private void assertFindUser() {
        final User user = this.userApi.findOne(MAIL);

        assertThat(user, is(notNullValue()));
        assertThat(user, is(userWith(MAIL, PASSWORD)));
    }

    private void assertRegisterNewUser() {
        assertThat(this.userApi.register(MAIL, PASSWORD), is(notNullValue()));
    }

    private TypeSafeMatcher<User> userWith(final String mail, final String password) {
        return new TypeSafeMatcher<User>() {
            @Override
            protected boolean matchesSafely(final User item) {
                return mail.equals(item.getMail()) && password.equals(item.getPassword());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(mail).appendValue(password);
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
