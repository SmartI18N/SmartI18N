package org.smarti18n.api;

import java.util.Base64;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class UserCredentials {

    public static final UserCredentials TEST = new UserCredentials("test@smarti18n.com", "test");

    private final String username;
    private final String password;

    public UserCredentials(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    String getBase64Credentials() {
        final String plainCredentials = username + ":" + password;

        return new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
