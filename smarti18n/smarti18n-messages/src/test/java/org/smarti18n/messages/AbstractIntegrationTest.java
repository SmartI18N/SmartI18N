package org.smarti18n.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.runner.RunWith;
import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserApiImpl;
import org.smarti18n.api.UserCredentials;
import org.smarti18n.api.UserCredentialsSupplier;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTest {

    protected static final String PROJECT_ID = "test";

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    User insertTestUser(final String username, final String password) {
        final UserApi userApi = new UserApiImpl(new TestRestTemplate().getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST));
        return userApi.register(
                username,
                password
        );
    }
}
