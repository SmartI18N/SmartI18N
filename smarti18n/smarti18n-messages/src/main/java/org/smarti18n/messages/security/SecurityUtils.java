package org.smarti18n.messages.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getUserMail() {
        final Object principal = authentication().getPrincipal();

        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUserMail();
        } else if (principal instanceof ProjectPrincipal) {
            return ((ProjectPrincipal) principal).getUserMail();
        } else {
            throw new IllegalStateException("unknown principal [" + principal + "]");
        }
    }

    public static String getProjectId() {
        final ProjectPrincipal principal = (ProjectPrincipal) authentication().getPrincipal();

        return principal.getProjectId();
    }

    private static Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
