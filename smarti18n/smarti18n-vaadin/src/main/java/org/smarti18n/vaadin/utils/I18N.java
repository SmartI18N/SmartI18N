package org.smarti18n.vaadin.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Service
public class I18N {

    private static MessageSource messageSource;

    public I18N(final MessageSource messageSource) {
        logger().info("Create and init static I18N class");

        I18N.messageSource = messageSource;
    }

    public static String translate(final String code, final String... args) {
        final String message = messageSource.getMessage(code, args, VaadinSession.getCurrent().getLocale());
        if (message.contains(code)) {
            logger().warn(code);
        }
        return message;
    }

    private static Logger logger() {
        return LoggerFactory.getLogger(I18N.class);
    }
}
