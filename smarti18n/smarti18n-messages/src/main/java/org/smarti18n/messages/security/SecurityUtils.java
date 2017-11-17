package org.smarti18n.messages.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class SecurityUtils {

    public static String getUserId() {
        final UserPrincipal principal = (UserPrincipal) authentication().getPrincipal();

        return principal.getUsername();
    }

    public static String getProjectId() {
        final ProjectPrincipal principal = (ProjectPrincipal) authentication().getPrincipal();

        return principal.getUsername();
    }

    private static Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
