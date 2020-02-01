package org.smarti18n.messages.users;

import org.smarti18n.api2.UsersApi;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.User;
import org.smarti18n.models.UserCreateDTO;
import org.smarti18n.models.UserSimplified;
import org.smarti18n.models.UserUpdateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Users2Endpoint implements UsersApi {

    private final UserService userService;

    public Users2Endpoint(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping(PATH_USERS_FIND_ALL)
    public List<User> findAll() {
        return userService.findAll();
    }

    @Override
    @GetMapping(PATH_USERS_FIND_ONE)
    public User findOne(
            @PathVariable("mail") String mail) throws UserUnknownException {

        return userService.findOne(mail);
    }

    @Override
    @GetMapping(PATH_USERS_FIND_ONE_SIMPLIFIED)
    public UserSimplified findOneSimplified(
            @PathVariable("mail") String mail) {

        return userService.findOneSimplified(mail);
    }

    @Override
    @PostMapping(PATH_USERS_CREATE)
    public User create(@RequestBody UserCreateDTO dto) throws UserExistException {

        return userService.register(dto);
    }

    @Override
    @PutMapping(PATH_USERS_UPDATE)
    public User update(
            @PathVariable("mail") String mail,
            @RequestBody UserUpdateDTO dto) throws UserUnknownException {

        return userService.update(mail, dto);
    }
}
