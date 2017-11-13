package org.smarti18n.messages.entities;

import org.springframework.data.annotation.Id;

import org.smarti18n.api.User;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class UserEntity implements User {

    @Id
    private String id;
    private String mail;
    private String password;
    private String vorname;
    private String nachname;
    private String company;

    public UserEntity(final String mail, final String password) {
        this.mail = mail;
        this.password = password;
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
}
