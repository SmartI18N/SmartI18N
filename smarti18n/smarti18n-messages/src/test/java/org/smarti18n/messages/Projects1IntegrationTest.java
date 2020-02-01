package org.smarti18n.messages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.api.ProjectsApiImpl;
import org.smarti18n.exceptions.ApiException;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectImpl;
import org.smarti18n.models.UserCredentials;
import org.smarti18n.models.UserCredentialsSupplier;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Projects1IntegrationTest extends AbstractIntegrationTest {

    private static final String NEW_PROJECT_ID = "NEW_PROJECT_ID".toLowerCase();
    private static final String NEW_PROJECT_NAME = "NEW_PROJECT_NAME";
    private static final String NEW_PROJECT_DESCRIPTION = "NEW_PROJECT_DESCRIPTION";
    private static final String NEW2_PROJECT_NAME = "NEW2_PROJECT_NAME";
    private static final String NEW2_PROJECT_DESCRIPTION = "NEW2_PROJECT_DESCRIPTION";

    private ProjectsApi projectsApi;

    @Before
    public void setUp() throws Exception {
        this.projectsApi = new ProjectsApiImpl(this.restTemplate.getRestTemplate(), this.port, new UserCredentialsSupplier(UserCredentials.TEST));
    }

    @Test
    public void standardWorkflowProjects() throws Exception {
        assertOnlyDefaultProjectsFound();
        assertCreateNewProject();
        assertUpdateProject();
        assertMessageFind();
        assertRemoveProject();
    }

    @Test(expected = ApiException.class)
    public void existInsertProject() throws Exception {
        assertOnlyDefaultProjectsFound();

        this.projectsApi.insert(NEW_PROJECT_ID, null);
        this.projectsApi.insert(NEW_PROJECT_ID, null);
    }

    @Test(expected = ApiException.class)
    public void unknownSaveProject() throws Exception {
        assertOnlyDefaultProjectsFound();

        final Project project = new ProjectImpl();
        project.setId(NEW_PROJECT_NAME);
        project.setName(NEW_PROJECT_NAME);
        project.setDescription(NEW_PROJECT_DESCRIPTION);

        this.projectsApi.update(project);
    }

    @Test
    public void testFindOnlyOwnProjects() throws Exception {
        this.projectsApi.insert(NEW_PROJECT_ID, null);

        final String test2User = "test2";

        insertTestUser(test2User, test2User);

        final ProjectsApiImpl projectsApi2 = new ProjectsApiImpl(
                this.restTemplate.getRestTemplate(),
                this.port,
                new UserCredentialsSupplier(new UserCredentials(test2User, test2User))
        );

        final List<? extends Project> projects = projectsApi2.findAll();
        assertThat(projects, hasSize(0));
    }

//
//    ASSERTS
//

    private void assertRemoveProject() throws ProjectUnknownException, UserUnknownException, UserRightsException {
        this.projectsApi.remove(NEW_PROJECT_ID);

        final List<Project> projects = new ArrayList<>(this.projectsApi.findAll());
        assertThat(projects, hasSize(2));
        assertThat(projects, hasItem(not(projectWith(NEW_PROJECT_ID))));
    }

    private void assertUpdateProject() throws UserUnknownException, UserRightsException, ProjectUnknownException {
        final Project project = this.projectsApi.findOne(NEW_PROJECT_ID);

        project.setName(NEW2_PROJECT_NAME);
        project.setDescription(NEW2_PROJECT_DESCRIPTION);

        this.projectsApi.update(project);

        final List<Project> projects = new ArrayList<>(this.projectsApi.findAll());
        assertThat(projects, hasSize(3));
        assertThat(projects, hasItem(projectWith(NEW_PROJECT_ID, NEW2_PROJECT_NAME, NEW2_PROJECT_DESCRIPTION)));
    }

    private void assertMessageFind() throws UserUnknownException, UserRightsException {
        final Project project = this.projectsApi.findOne(NEW_PROJECT_ID);

        assertThat(project, is(notNullValue()));
        assertThat(project, is(projectWith(NEW_PROJECT_ID)));
    }

    private void assertCreateNewProject() throws UserUnknownException, ProjectExistException {
        this.projectsApi.insert(NEW_PROJECT_ID, null);

        final List<Project> projects = new ArrayList<>(this.projectsApi.findAll());
        assertThat(projects, hasSize(3));
        assertThat(projects, hasItem(projectWith(NEW_PROJECT_ID)));
    }

    private void assertOnlyDefaultProjectsFound() throws UserUnknownException {
        final List<Project> projects = this.projectsApi.findAll();

        assertThat(projects, hasSize(2));
        assertThat(projects, hasItem(projectWith("test")));
        assertThat(projects, hasItem(projectWith("default")));
    }

//
//    MATCHERS
//

    private Matcher<Project> projectWith(final String projectId) {
        return new TypeSafeMatcher<Project>() {
            @Override
            protected boolean matchesSafely(final Project item) {
                return projectId.equals(item.getId()) && !StringUtils.isEmpty(item.getSecret());
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

}
