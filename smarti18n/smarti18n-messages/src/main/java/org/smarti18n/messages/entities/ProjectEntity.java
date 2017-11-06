package org.smarti18n.messages.entities;

import org.smarti18n.api.Project;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Locale;
import java.util.Set;

@Document
public class ProjectEntity implements Project {

    @Id
    private String id;

    private String name;
    private String description;

    private Set<Locale> locales;

    ProjectEntity() {
    }

    public ProjectEntity(final String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Set<Locale> getLocales() {
        return locales;
    }

    @Override
    public void setLocales(final Set<Locale> locales) {
        this.locales = locales;
    }
}
