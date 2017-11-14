package org.smarti18n.api;

import java.util.Locale;
import java.util.Map;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface SpringMessagesApi {

    String PATH_MESSAGES_FIND_SPRING = "/api/1/messages/findForSpringMessageSource";

    Map<String, Map<Locale, String>> findForSpringMessageSource();

}
