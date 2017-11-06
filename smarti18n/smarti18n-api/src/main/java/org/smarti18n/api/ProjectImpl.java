package org.smarti18n.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ProjectImpl implements Project {

    private String id;
    private String name;
    private String description;
    private Set<Locale> locales;

    public ProjectImpl() {
    }

    public ProjectImpl(final Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.locales = new HashSet<>(
                project.getLocales()
        );
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name= name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(final String description) {
        this.description= description;
    }

    @Override
    public Set<Locale> getLocales() {
        return this.locales;
    }

    @Override
    public void setLocales(final Set<Locale> locales) {
        this.locales = locales;
    }
}
