package org.smarti18n.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public interface MessagesApi {

    String root();

    MessageSimple findOne(
            String key,
            Locale language
    );

    Collection<MessageTranslations> findAll();

    MessageTranslations save(
            String key,
            String translation,
            Locale language
    );
}
