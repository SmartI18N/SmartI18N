package org.smarti18n.messages.service;

import java.util.List;

import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.User;
import org.smarti18n.models.UserSimplified;

public interface UserService {

    List<User> findAll();

    User findOne(
            String mail
    ) throws UserUnknownException;

    UserSimplified findOneSimplified(
            String mail
    );

    User register(
            String mail,
            final String password) throws UserExistException;

    User update(
            User user
    ) throws UserUnknownException;
}
