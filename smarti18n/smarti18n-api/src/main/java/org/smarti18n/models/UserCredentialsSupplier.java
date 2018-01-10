package org.smarti18n.models;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class UserCredentialsSupplier implements Serializable {

    private UserCredentials userCredentials;

    public UserCredentialsSupplier() {
    }

    public UserCredentialsSupplier(final UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getBase64Credentials() {
        Assert.notNull(userCredentials, "UserCredentials");

        return userCredentials.getBase64Credentials();
    }

    public void setUserCredentials(final UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }
}
