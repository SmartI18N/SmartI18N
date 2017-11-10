package org.smarti18n.editor.vaadin;

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

    private final Logger logger = LoggerFactory.getLogger(I18N.class);

    public I18N(final MessageSource messageSource) {
        this.logger.info("Create and init static I18N class");

        I18N.messageSource = messageSource;
    }

    public static String getMessage(final String code, final String... args) {
        return messageSource.getMessage(code, args, VaadinSession.getCurrent().getLocale());
    }

}
