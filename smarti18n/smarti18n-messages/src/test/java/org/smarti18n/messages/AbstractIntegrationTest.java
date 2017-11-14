package org.smarti18n.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.runner.RunWith;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApiImpl;
import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.api.UserApiImpl;
import org.smarti18n.api.UserCredentials;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    User insertTestUser() {
        final UserApi userApi = new UserApiImpl(new TestRestTemplate().getRestTemplate(), this.port, () -> UserCredentials.TEST);
        return userApi.register(
                UserCredentials.TEST.getUsername(),
                UserCredentials.TEST.getPassword()
        );
    }

    Project insertTestProject(final String projectId) {
        final ProjectsApiImpl projectsApi = new ProjectsApiImpl(new TestRestTemplate().getRestTemplate(), this.port, () -> UserCredentials.TEST);
        return projectsApi.insert(projectId);
    }

}
