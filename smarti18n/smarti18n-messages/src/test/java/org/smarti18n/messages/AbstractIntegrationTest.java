package org.smarti18n.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.smarti18n.api.ApiExceptionHandler;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.models.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserApiImpl;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;

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

    @Before
    public void setUpRestTemplate() throws Exception {
        this.restTemplate.getRestTemplate().setErrorHandler(new ApiExceptionHandler());
    }

    User insertTestUser(final String username, final String password) throws UserExistException {
        final UserApi userApi = new UserApiImpl(restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST));

        return userApi.register(
                username,
                password
        );
    }
}
