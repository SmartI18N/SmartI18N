package org.smarti18n.api.impl;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;

public class ProjectsApiImpl implements ProjectsApi {

    public final static String DEFAULT_HOST = "https://messages.smarti18n.com";

    private final String host;
    private final RestTemplate restTemplate;

    public ProjectsApiImpl(final Environment environment, final RestTemplate restTemplate) {
        this.host = environment.getProperty("", DEFAULT_HOST);
        this.restTemplate = restTemplate;
    }

    public ProjectsApiImpl(final RestTemplate restTemplate, final int port) {
        this.host = "http://localhost:" + port;
        this.restTemplate = restTemplate;
    }

    @Override
    public Collection<? extends Project> findAll() {
        return Arrays.asList(
                this.restTemplate.getForObject(host + ProjectsApi.PATH_PROJECTS_FIND_ALL, ProjectImpl[].class)
        );
    }

    @Override
    public Project save(final Project project) {
        return this.restTemplate.postForObject(host + ProjectsApi.PATH_PROJECTS_SAVE, project, ProjectImpl.class);
    }
}
