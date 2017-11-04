package org.smarti18n.editor.vaadin;

import org.smarti18n.api.spring.Smarti18nMessageSource;

import com.vaadin.server.VaadinSession;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class I18N {

    private final MessageSource messageSource;

    public I18N(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(final String code, final String... args) {
        return this.messageSource.getMessage(code, args, VaadinSession.getCurrent().getLocale());
    }

    public void refreshMessageSource() {
        if (this.messageSource instanceof Smarti18nMessageSource) {
            ((Smarti18nMessageSource)this.messageSource).refreshMessageSource();
        }
    }

}
