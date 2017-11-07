package org.smarti18n.api;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ProjectImpl implements Project {

    private String id;
    private String name;
    private String description;
    private Set<Locale> locales;
    private Set<String> secrets;

    public ProjectImpl() {
    }

    public ProjectImpl(final Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.locales = project.getLocales() == null ? new HashSet<>() : new HashSet<>(project.getLocales());
        this.secrets = project.getSecrets() == null ? new HashSet<>() : new HashSet<>(project.getSecrets());
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
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Set<Locale> getLocales() {
        return this.locales;
    }

    @Override
    public void setLocales(final Set<Locale> locales) {
        this.locales = locales;
    }

    @Override
    public Set<String> getSecrets() {
        return secrets;
    }

    @Override
    public void setSecrets(final Set<String> secrets) {
        this.secrets = secrets;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectImpl)) {
            return false;
        }

        final ProjectImpl project = (ProjectImpl) o;

        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
