package org.smarti18n.api.v2;

import org.smarti18n.api.AbstractApiImpl;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.User;
import org.smarti18n.models.UserCreateDTO;
import org.smarti18n.models.UserCredentialsSupplier;
import org.smarti18n.models.UserSimplified;
import org.smarti18n.models.UserUpdateDTO;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class UsersApiImpl extends AbstractApiImpl implements UsersApi {

    public UsersApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, host, userCredentialsSupplier);
    }

    public UsersApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, port, userCredentialsSupplier);
    }

    @Override
    public List<User> findAll() {
        return Arrays.asList(
                get(uri(PATH_USERS_FIND_ALL), User[].class)
        );
    }

    @Override
    public User findOne(String mail) throws UserUnknownException {
        return get(uri(path(PATH_USERS_FIND_ONE, mail)), User.class);
    }

    @Override
    public UserSimplified findOneSimplified(String mail) {
        return get(uri(path(PATH_USERS_FIND_ONE_SIMPLIFIED, mail)), UserSimplified.class);
    }

    @Override
    public User create(UserCreateDTO dto) throws UserExistException {
        return post(uri(PATH_USERS_CREATE), dto, User.class);
    }

    @Override
    public User update(String mail, UserUpdateDTO dto) throws UserUnknownException {
        return put(uri(path(PATH_USERS_UPDATE, mail)), dto, User.class);
    }

    private static String path(String path, String mail) {
        return path.replace("{mail}", mail);
    }
}
