package org.smarti18n.api;

import java.util.function.Supplier;

import org.springframework.util.Assert;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface UserCredentialsSupplier extends Supplier<UserCredentials> {

    default String getBase64Credentials() {
        final UserCredentials userCredentials = get();
        Assert.notNull(userCredentials, "UserCredentials");

        return userCredentials.getBase64Credentials();
    }

}
