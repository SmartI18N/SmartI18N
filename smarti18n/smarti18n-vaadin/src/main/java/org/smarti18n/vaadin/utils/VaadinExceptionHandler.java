package org.smarti18n.vaadin.utils;

import com.vaadin.ui.Notification;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public final class VaadinExceptionHandler {
    private VaadinExceptionHandler() {
    }

    private static void showNotification(final String errorTextKey) {
        Notification.show(
                I18N.translate("notification.error.title"),
                I18N.translate("notification.error." + errorTextKey),
                Notification.Type.ERROR_MESSAGE
        );
    }

    public static void handleValidationException() {
        showNotification("validation");
    }

    public static void handleUserRightsException() {
        showNotification("user-rights");
    }

    public static void handleUserUnknownException() {
        showNotification("user-unknown");
    }

    public static void handleProjectUnknownException() {
        showNotification("project-unknown");
    }

    public static void handleMessageUnknownException() {
        showNotification("message-unknown");
    }

    public static void handleProjectExistException() {
        showNotification("project-exist");
    }

    public static void handleMessageExistException() {
        showNotification("message-exist");
    }

    public static void handleUserExistException() {
        showNotification("user-exist");
    }
}
