package org.smarti18n.messages.service;

import org.smarti18n.api.User;

public interface UserService {

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
