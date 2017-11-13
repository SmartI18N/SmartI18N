package org.smarti18n.api;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public class UserApiImpl extends AbstractApiImpl implements UserApi {

    public UserApiImpl(final Environment environment, final RestTemplate restTemplate) {
        super(restTemplate, environment);
    }

    public UserApiImpl(final RestTemplate restTemplate, final int port) {
        super(restTemplate, "http://localhost:" + port, "test", "test");
    }

    @Override
    public User findOne(final String mail) {
        return get(uri(PATH_USERS_FIND_ONE).queryParam("mail", mail), User.class);
    }

    @Override
    public User register(final String mail, final String password) {
        return get(uri(PATH_USERS_REGISTER).queryParam("mail", mail).queryParam("password", password), User.class);
    }

    @Override
    public User update(final User user) {
        return post(uri(PATH_USERS_UPDATE), user, User.class);
    }
}
