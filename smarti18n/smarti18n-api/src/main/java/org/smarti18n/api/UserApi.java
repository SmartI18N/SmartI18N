package org.smarti18n.api;

public interface UserApi {

    String PATH_USERS_FIND_ONE = "/api/1/users/findOne";
    String PATH_USERS_REGISTER = "/api/1/users/register";
    String PATH_USERS_UPDATE = "/api/1/users/update";

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
