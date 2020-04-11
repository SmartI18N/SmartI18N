package org.smarti18n.api.v2;

import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.User;
import org.smarti18n.models.UserCreateDTO;
import org.smarti18n.models.UserSimplified;
import org.smarti18n.models.UserUpdateDTO;

import java.util.List;

public interface UsersApi {

    String PATH_USERS_FIND_ALL = "/api/2/users";
    String PATH_USERS_FIND_ONE = "/api/2/users/{mail}";
    String PATH_USERS_CREATE = "/api/2/users";
    String PATH_USERS_UPDATE = "/api/2/users/{mail}";
    String PATH_USERS_FIND_ONE_SIMPLIFIED = "/api/2/simple-users/{mail}";

    List<User> findAll();

    User findOne(
            String mail
    ) throws UserUnknownException;

    UserSimplified findOneSimplified(
            String mail
    );

    User create(
            UserCreateDTO dto
    ) throws UserExistException;

    User update(
            String mail,
            UserUpdateDTO dto
    ) throws UserUnknownException;
}
