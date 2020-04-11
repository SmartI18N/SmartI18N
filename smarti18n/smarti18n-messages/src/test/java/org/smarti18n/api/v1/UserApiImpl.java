package org.smarti18n.api.v1;

import java.util.Arrays;
import java.util.List;

import org.smarti18n.api.AbstractApiImpl;
import org.smarti18n.api.v1.UserApi;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.smarti18n.models.User;
import org.smarti18n.models.UserCredentialsSupplier;
import org.smarti18n.models.UserSimplified;

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
    public List<User> findAll() {
        final UriComponentsBuilder uri = uri(PATH_USERS_FIND_ALL);

        return Arrays.asList(
                get(uri, User[].class)
        );
    }

    @Override
    public User findOne(final String mail) {
        final UriComponentsBuilder uri = uri(PATH_USERS_FIND_ONE)
                .queryParam("mail", mail);

        return get(uri, User.class);
    }

    @Override
    public UserSimplified findOneSimplified(final String mail) {
        final UriComponentsBuilder uri = uri(PATH_USERS_FIND_ONE_SIMPLIFIED)
                .queryParam("mail", mail);

        final ResponseEntity<UserSimplified> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.GET,
                null,
                UserSimplified.class
        );

        return handleResponse(exchange);
    }

    @Override
    public User register(final String mail, final String password) {
        final UriComponentsBuilder uri = uri(PATH_USERS_REGISTER)
                .queryParam("mail", mail).queryParam("password", password);

        return get(uri, User.class);
    }

    @Override
    public User update(final User user) {
        return post(uri(PATH_USERS_UPDATE), user, User.class);
    }
}
