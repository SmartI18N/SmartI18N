package org.smarti18n.api;

import java.util.Locale;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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

    String getSecret();

    void setSecret(String secret);

    default String getDisplayName() {
        if (StringUtils.isEmpty(getName())) {
            return getId();
        } else {
            return getName();
        }
    }
}
