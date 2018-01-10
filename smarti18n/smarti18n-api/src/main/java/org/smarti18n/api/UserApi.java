package org.smarti18n.api;

import java.util.List;

import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.User;
import org.smarti18n.models.UserSimplified;

public interface UserApi {

    String PATH_USERS_FIND_ALL = "/api/1/users/findAll";
    String PATH_USERS_FIND_ONE = "/api/1/users/findOne";
    String PATH_USERS_FIND_ONE_SIMPLIFIED = "/api/1/users/findOneSimplified";
    String PATH_USERS_REGISTER = "/api/1/users/register";
    String PATH_USERS_UPDATE = "/api/1/users/update";

    List<User> findAll();

    User findOne(
            String mail
    ) throws UserUnknownException;

    UserSimplified findOneSimplified(
            String mail
    );

    User register(
            String mail,
            String password
    ) throws UserExistException;

    User update(
            User user
    ) throws UserUnknownException;
}
