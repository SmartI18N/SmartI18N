package org.smarti18n.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(as = UserImpl.class)
public interface User extends Serializable {

    String getId();

    void setId(String id);

    String getMail();

    void setMail(String mail);

    String getPassword();

    void setPassword(String password);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getCompany();

    void setCompany(String company);

    UserRole getRole();

    void setRole(UserRole role);
}
