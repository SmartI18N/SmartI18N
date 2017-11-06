package org.smarti18n.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Locale;
import java.util.Set;

@JsonDeserialize(as = ProjectImpl.class)
public interface Project {

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    Set<Locale> getLocales();

    void setLocales(Set<Locale> locales);
}
