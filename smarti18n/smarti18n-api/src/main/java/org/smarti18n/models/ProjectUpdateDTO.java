package org.smarti18n.models;

import java.util.Locale;
import java.util.Set;

public class ProjectUpdateDTO {

    private String name;
    private String description;
    private Set<Locale> locales;

    public ProjectUpdateDTO() {
    }

    public ProjectUpdateDTO(String name, String description, Set<Locale> locales) {
        this.name = name;
        this.description = description;
        this.locales = locales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Locale> getLocales() {
        return locales;
    }

    public void setLocales(Set<Locale> locales) {
        this.locales = locales;
    }
}
