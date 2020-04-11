package org.smarti18n.models;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface User extends Serializable {

    String getId();

    void setId(String id);

    String getMail();

    void setMail(String mail);

    String getPassword();

    void setPassword(String password);

    // TODO: rename
    @Deprecated
    String getVorname();

    // TODO: rename
    @Deprecated
    void setVorname(String vorname);

    // TODO: rename
    @Deprecated
    String getNachname();

    // TODO: rename
    @Deprecated
    void setNachname(String nachname);

    String getCompany();

    void setCompany(String company);

    UserRole getRole();

    void setRole(UserRole role);
}
