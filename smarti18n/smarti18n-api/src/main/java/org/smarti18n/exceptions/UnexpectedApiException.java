package org.smarti18n.exceptions;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class UnexpectedApiException extends RuntimeException {

    public UnexpectedApiException(final String message) {
        super(message);
    }
}
