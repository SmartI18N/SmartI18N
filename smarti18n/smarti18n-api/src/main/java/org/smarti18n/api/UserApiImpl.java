package org.smarti18n.api;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UserApiImpl extends AbstractApiImpl implements UserApi {

    public UserApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, host, userCredentialsSupplier);
    }

    public UserApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, port, userCredentialsSupplier);
    }

    @Override
    public User findOne(final String mail) {
        final UriComponentsBuilder uri = uri(PATH_USERS_FIND_ONE)
                .queryParam("mail", mail);

        final ResponseEntity<User> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.GET,
                null,
                User.class
        );

        return handleResponse(exchange);
    }

    @Override
    public User register(final String mail, final String password) {
        final UriComponentsBuilder uri = uri(PATH_USERS_REGISTER)
                .queryParam("mail", mail).queryParam("password", password);

        final ResponseEntity<User> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.GET,
                null,
                User.class
        );

        return handleResponse(exchange);
    }

    @Override
    public User update(final User user) {
        return post(uri(PATH_USERS_UPDATE), user, User.class);
    }
}
