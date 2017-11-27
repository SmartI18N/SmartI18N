package org.smarti18n.api;

public class UserImpl implements User {

    private String id;
    private String mail;
    private String password;
    private String vorname;
    private String nachname;
    private String company;
    private UserRole role;

    public UserImpl() {
    }

    public UserImpl(final String id) {
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
    public String getVorname() {
        return vorname;
    }

    @Override
    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }

    @Override
    public String getNachname() {
        return nachname;
    }

    @Override
    public void setNachname(final String nachname) {
        this.nachname = nachname;
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
        return this.role;
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
        if (!(o instanceof UserImpl)) {
            return false;
        }

        final UserImpl user = (UserImpl) o;

        return id.equals(user.id);
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
