package org.smarti18n.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface User {

    String getId();

    void setId(String id);

    String getMail();

    void setMail(String mail);

    String getPassword();

    void setPassword(String password);

    String getVorname();

    void setVorname(String vorname);

    String getNachname();

    void setNachname(String nachname);

    String getCompany();

    void setCompany(String company);
}
