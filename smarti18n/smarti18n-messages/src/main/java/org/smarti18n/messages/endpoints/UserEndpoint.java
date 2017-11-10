package org.smarti18n.messages.endpoints;

import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.messages.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserEndpoint implements UserApi {

    private final UserService userService;

    public UserEndpoint(final UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping(PATH_USERS_LOG_GITHUB_LOGIN)
    public void logGitHubLogin(
            @RequestBody final User user) {

        this.userService.logGitHubLogin(user);
    }
}
