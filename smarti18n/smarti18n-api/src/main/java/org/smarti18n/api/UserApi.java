package org.smarti18n.api;

import java.util.List;

public interface UserApi {

    String PATH_USERS_FIND_ALL = "/api/1/users/findAll";
    String PATH_USERS_FIND_ONE = "/api/1/users/findOne";
    String PATH_USERS_REGISTER = "/api/1/users/register";
    String PATH_USERS_UPDATE = "/api/1/users/update";

    List<User> findAll();

    User findOne(
            String mail
    );

    User register(
            String mail,
            String password
    );

    User update(
            User user
    );
}
