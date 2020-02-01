package org.smarti18n.models;

public class UserUpdateDTO {

    private String vorname;
    private String nachname;
    private String company;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String vorname, String nachname, String company) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.company = company;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
