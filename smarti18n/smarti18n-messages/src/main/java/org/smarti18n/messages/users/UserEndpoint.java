package org.smarti18n.messages.users;

import org.smarti18n.api.UserApi;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.User;
import org.smarti18n.models.UserCreateDTO;
import org.smarti18n.models.UserSimplified;
import org.smarti18n.models.UserUpdateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserEndpoint implements UserApi {

    private final UserService userService;

    public UserEndpoint(final UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping(PATH_USERS_FIND_ALL)
    public List<User> findAll() {
        return this.userService.findAll();
    }

    @Override
    @GetMapping(PATH_USERS_FIND_ONE)
    public User findOne(
            @RequestParam("mail") final String mail
    ) throws UserUnknownException {

        return userService.findOne(mail);
    }

    @Override
    @GetMapping(PATH_USERS_FIND_ONE_SIMPLIFIED)
    public UserSimplified findOneSimplified(
            @RequestParam("mail") final String mail) {

        return userService.findOneSimplified(mail);
    }

    @Override
    @GetMapping(PATH_USERS_REGISTER)
    public User register(
            @RequestParam("mail") final String mail,
            @RequestParam("password") final String password
    ) throws UserExistException {

        return userService.register(
                new UserCreateDTO(mail, password)
        );
    }

    @Override
    @PostMapping(PATH_USERS_UPDATE)
    public User update(
            @RequestBody final User user
    ) throws UserUnknownException {

        return this.userService.update(
                user.getMail(),
                new UserUpdateDTO(user.getVorname(), user.getNachname(), user.getCompany())
        );
    }
}
