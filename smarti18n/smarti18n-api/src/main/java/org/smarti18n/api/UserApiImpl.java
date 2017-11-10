package org.smarti18n.api;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public class UserApiImpl extends AbstractApiImpl implements UserApi {

    public UserApiImpl(final Environment environment, final RestTemplate restTemplate) {
        super(
                restTemplate,
                environment.getProperty("", DEFAULT_HOST),
                environment.getProperty("", DEFAULT_USERNAME),
                environment.getProperty("", DEFAULT_PASSWORD)
        );
    }

    public UserApiImpl(final RestTemplate restTemplate, final int port) {
        super(restTemplate, "http://localhost:" + port, "test", "test");
    }

    @Override
    public void logGitHubLogin(final User user) {
        post(uri(PATH_USERS_LOG_GITHUB_LOGIN), user, Void.class);
    }
}
