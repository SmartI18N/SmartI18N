package org.smarti18n.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface User {
    String getId();

    void setId(String id);

    String getLogin();

    void setLogin(String login);

    String getEmail();

    void setEmail(String email);

    String getName();

    void setName(String name);

    String getCompany();

    void setCompany(String company);

    String getType();

    void setType(String type);
}
