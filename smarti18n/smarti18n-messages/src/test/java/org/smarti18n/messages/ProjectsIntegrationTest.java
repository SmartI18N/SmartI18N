package org.smarti18n.messages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.ApiException;
import org.smarti18n.api.ProjectsApiImpl;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class ProjectsIntegrationTest extends AbstractIntegrationTest {

    private static final String PROJECT_ID = "PROJECT_ID";
    private static final String PROJECT_NAME = "PROJECT_NAME";
    private static final String PROJECT_DESCRIPTION = "PROJECT_DESCRIPTION";
    private static final String NEW_PROJECT_NAME = "NEW_PROJECT_NAME";
    private static final String NEW_PROJECT_DESCRIPTION = "NEW_PROJECT_DESCRIPTION";

    private ProjectsApi projectsApi;

    @Before
    public void setUp() throws Exception {
        this.projectsApi = new ProjectsApiImpl(new TestRestTemplate().getRestTemplate(), this.port);
    }

    @Test
    public void standardWorkflowProjects() throws Exception {
        assertNoProjectsFound();
        assertCreateNewProject();
        assertUpdateProject();
        assertGenerateSecret();
    }

    @Test(expected = ApiException.class)
    public void existInsertProject() throws Exception {
        assertNoProjectsFound();

        this.projectsApi.insert(PROJECT_ID);
        this.projectsApi.insert(PROJECT_ID);
    }

    @Test(expected = ApiException.class)
    public void unknownSaveProject() throws Exception {
        assertNoProjectsFound();

        final Project project = new ProjectImpl();
        project.setId(PROJECT_NAME);
        project.setName(PROJECT_NAME);
        project.setDescription(PROJECT_DESCRIPTION);

        this.projectsApi.update(project);
    }

    @Test(expected = ApiException.class)
    public void unknownGenerateSecret() throws Exception {
        assertNoProjectsFound();

        this.projectsApi.generateSecret(PROJECT_ID);
    }

//
//    ASSERTS
//

    private void assertGenerateSecret() {
        final String secret = this.projectsApi.generateSecret(PROJECT_ID);

        final List<Project> projects = new ArrayList<>(this.projectsApi.findAll());
        assertThat(projects, hasSize(2));
        assertThat(projects, hasItem(projectWith(PROJECT_ID, secret)));
    }

    private void assertUpdateProject() {
        final Project project = this.projectsApi.findAll().stream()
                .filter(project1 -> PROJECT_ID.equals(project1.getId())).findFirst().get();

        project.setName(NEW_PROJECT_NAME);
        project.setDescription(NEW_PROJECT_DESCRIPTION);

        this.projectsApi.update(project);

        final List<Project> projects = new ArrayList<>(this.projectsApi.findAll());
        assertThat(projects, hasSize(2));
        assertThat(projects, hasItem(projectWith(PROJECT_ID, NEW_PROJECT_NAME, NEW_PROJECT_DESCRIPTION)));
    }

    private void assertCreateNewProject() {
        this.projectsApi.insert(PROJECT_ID);

        final List<Project> projects = new ArrayList<>(this.projectsApi.findAll());
        assertThat(projects, hasSize(2));
        assertThat(projects, hasItem(projectWith(PROJECT_ID)));
    }

    private void assertNoProjectsFound() {
        assertThat(this.projectsApi.findAll(), hasSize(1));
    }

//
//    MATCHERS
//

    private Matcher<Project> projectWith(final String projectId) {
        return new TypeSafeMatcher<Project>() {
            @Override
            protected boolean matchesSafely(final Project item) {
                return projectId.equals(item.getId());
            }

            @Override
            public void describeTo(final Description description) {

            }
        };
    }

    private Matcher<Project> projectWith(final String projectId, final String projectName, final String projectDescription) {
        return new TypeSafeMatcher<Project>() {
            @Override
            protected boolean matchesSafely(final Project project) {
                return projectId.equals(project.getId()) && projectName.equals(project.getName())
                        && projectDescription.equals(project.getDescription());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(projectId).appendValue(projectName).appendValue(projectDescription);
            }
        };
    }

    private Matcher<Project> projectWith(final String projectId, final String secret) {
        return new TypeSafeMatcher<Project>() {
            @Override
            protected boolean matchesSafely(final Project item) {
                return projectId.equals(item.getId()) && item.getSecrets().contains(secret);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(projectId).appendValue(secret);
            }
        };
    }

}