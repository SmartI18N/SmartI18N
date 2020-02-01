package org.smarti18n.messages.common;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public final class IdentifierUtils {

    private IdentifierUtils() {
    }

    public static String clean(final String identifier) {
        return identifier.trim().toLowerCase();
    }
}
