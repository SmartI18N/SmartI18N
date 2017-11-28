package org.smarti18n.messages.entities;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import org.smarti18n.api.Project;
import org.smarti18n.api.User;
import org.smarti18n.api.UserRole;

@Document(collection = "projects")
public class ProjectEntity implements Project {

    @Id
    private String id;

    private String name;
    private String description;
    private String secret;

    private Set<Locale> locales = new HashSet<>();

    @DBRef
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

    @Override
    public Set<User> getOwners() {
        return new HashSet<>(owners);
    }

    @Override
    public void setOwners(final Set<User> owners) {
        this.owners = owners.stream().map(UserEntity::new).collect(Collectors.toSet());
    }

    public void addOwner(final UserEntity user) {
        this.owners.add(user);
    }

    public boolean hasOwner(final UserEntity user) {
        return user.getRole() == UserRole.SUPERUSER || this.owners.contains(user);
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
