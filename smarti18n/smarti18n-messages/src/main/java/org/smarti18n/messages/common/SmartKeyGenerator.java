package org.smarti18n.messages.common;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Service
public class SmartKeyGenerator {

    private final KeyGenerator keyGenerator;

    public SmartKeyGenerator() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(256);
    }

    public String generateKey() {
        return Base64.getUrlEncoder().encodeToString(
                this.keyGenerator.generateKey().getEncoded()
        );
    }
}
