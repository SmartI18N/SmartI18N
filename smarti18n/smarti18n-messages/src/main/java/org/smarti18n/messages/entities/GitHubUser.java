package org.smarti18n.messages.entities;

import org.smarti18n.api.User;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "github_users")
public class GitHubUser implements User {

    @Id
    private String id;

    private String login;
    private String email;
    private String name;
    private String company;
    private String type;

    private List<Date> logins = new ArrayList<>();

    public GitHubUser() {
    }

    public GitHubUser(final String id) {
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
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(final String login) {
        this.login = login;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(final String email) {
        this.email = email;
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
    public String getCompany() {
        return company;
    }

    @Override
    public void setCompany(final String company) {
        this.company = company;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(final String type) {
        this.type = type;
    }

    public List<Date> getLogins() {
        return logins;
    }

    public void setLogins(final List<Date> logins) {
        this.logins = logins;
    }

    public void addLogin() {
        this.logins.add(new Date());
    }
}
