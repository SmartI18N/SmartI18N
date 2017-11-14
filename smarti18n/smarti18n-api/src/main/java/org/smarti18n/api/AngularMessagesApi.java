package org.smarti18n.api;

import java.util.Locale;
import java.util.Map;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface AngularMessagesApi {

    String PATH_MESSAGES_FIND_ANGULAR = "/api/1/messages/findForAngularMessageSource";

    Map<String, String> getMessages(
            String projectId,
            Locale locale
    );
}
