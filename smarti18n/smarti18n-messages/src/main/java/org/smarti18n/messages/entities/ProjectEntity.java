package org.smarti18n.messages.entities;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.smarti18n.api.Project;

@Document(collection = "projects")
public class ProjectEntity implements Project {

    @Id
    private String id;

    private String name;
    private String description;
    private String secret;

    private Set<Locale> locales = new HashSet<>();
    private Set<UserEntity> owners = new HashSet<>();

    ProjectEntity() {
    }

    public ProjectEntity(final String id, final String secret, final UserEntity user) {
        this.id = id;
        this.secret = secret;
        this.owners.add(user);
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

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public Set<UserEntity> getOwners() {
        return owners;
    }

    public void setOwners(final Set<UserEntity> owners) {
        this.owners = owners;
    }

    public void addOwner(final UserEntity user) {
        this.owners.add(user);
    }

    public boolean hasOwner(final UserEntity user) {
        return this.owners.contains(user);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectEntity)) {
            return false;
        }

        final ProjectEntity that = (ProjectEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
