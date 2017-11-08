package org.smarti18n.messages.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;

/**
 * @author Marc Bellmann &lt;marc.bellmann@saxess.ag&gt;
 */
@Service
public class ProjectKeyGenerator {

    private final KeyGenerator keyGenerator;

    public ProjectKeyGenerator() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(256);
    }

    String generateKey() {
        return Base64.getUrlEncoder().encodeToString(
                this.keyGenerator.generateKey().getEncoded()
        );
    }
}
