package org.smarti18n.messages;

import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.impl.MessagesApiImpl;
import org.smarti18n.api.impl.ProjectsApiImpl;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MessagesIntegrationTest {

    public static final String PROJECT_ID = "test";
    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    private MessagesApi messagesApi;

    private ProjectsApi projectsApi;

    @Before
    public void setUp() throws Exception {
        this.messagesApi = new MessagesApiImpl(new TestRestTemplate().getRestTemplate(), this.port);
        this.projectsApi = new ProjectsApiImpl(new TestRestTemplate().getRestTemplate(), this.port);
    }

    @Test
    public void testProjects() throws Exception {
        assertThat(this.projectsApi.findAll(), is(empty()));

        final Project project = new ProjectImpl();
        project.setId(PROJECT_ID);

        this.projectsApi.save(project);

        assertThat(this.projectsApi.findAll().iterator().next(), is(projectMatcher(PROJECT_ID)));
    }

    private Matcher<Project> projectMatcher(final String projectId) {
        return new TypeSafeMatcher<Project>() {
            @Override
            protected boolean matchesSafely(final Project project) {
                return project.getId().equals(projectId);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(projectId);
            }
        };
    }
}