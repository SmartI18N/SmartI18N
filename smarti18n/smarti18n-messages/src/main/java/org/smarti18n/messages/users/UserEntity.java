package org.smarti18n.messages.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.smarti18n.models.User;
import org.smarti18n.models.UserRole;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Document(collection = "users")
public class UserEntity implements User {

    @Id
    private String id;
    private String mail;
    private String password;
    private String vorname;
    private String nachname;
    private String company;
    private UserRole role;

    public UserEntity() {
    }

    public UserEntity(final String mail, final String password, final UserRole role) {
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public UserEntity(final User user) {
        this.id = user.getId();
        this.mail = user.getMail();
        this.password = user.getPassword();
        this.vorname = user.getFirstName();
        this.nachname = user.getLastName();
        this.company = user.getCompany();
        this.role = user.getRole();
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
    public String getMail() {
        return mail;
    }

    @Override
    public void setMail(final String mail) {
        this.mail = mail;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String getFirstName() {
        return vorname;
    }

    @Override
    public void setFirstName(final String firstName) {
        this.vorname = firstName;
    }

    @Override
    public String getLastName() {
        return nachname;
    }

    @Override
    public void setLastName(final String lastName) {
        this.nachname = lastName;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public void setCompany(final String company) {
        this.company = company;
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public void setRole(final UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }

        final UserEntity that = (UserEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return mail;
    }
}
