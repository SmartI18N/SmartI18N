package org.smarti18n.api;

public interface UserApi {

    String PATH_USERS_LOG_GITHUB_LOGIN = "/api/1/users/logGitHubLogin";

    void logGitHubLogin(
            User user
    );

}
