package org.smarti18n.messages.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;
import org.smarti18n.messages.service.UserService;

@RestController
public class UserEndpoint implements UserApi {

    private final UserService userService;

    public UserEndpoint(final UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping(PATH_USERS_FIND_ONE)
    public User findOne(
            @RequestParam("mail") final String mail) {

        return userService.findOne(mail);
    }

    @Override
    @GetMapping(PATH_USERS_REGISTER)
    public User register(
            @RequestParam("mail") final String mail,
            @RequestParam("password") final String password) {

        return userService.register(mail, password);
    }

    @Override
    @PostMapping(PATH_USERS_UPDATE)
    public User update(
            @RequestBody final User user) {

        return this.userService.update(user);
    }
}
