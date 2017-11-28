package org.smarti18n.messages.service;

import java.util.List;

import org.smarti18n.api.User;

public interface UserService {

    List<User> findAll();

    User findOne(
            String mail
    );

    User register(
            String mail,
            final String password);

    User update(
            User user
    );
}
