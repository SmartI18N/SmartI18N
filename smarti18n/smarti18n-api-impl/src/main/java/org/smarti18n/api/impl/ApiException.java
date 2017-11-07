package org.smarti18n.api.impl;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ApiException extends RuntimeException {

    public ApiException(final String message) {
        super(message);
    }
}