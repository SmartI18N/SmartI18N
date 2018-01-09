package org.smarti18n.api;

/**
 * @author Marc Bellmann &lt;marc.bellmann@saxess.ag&gt;
 */
public class UserSimplified {

    private String mail;
    private String password;
    private UserRole role;

    public UserSimplified() {
    }

    public UserSimplified(final User user) {
        mail = user.getMail();
        password = user.getPassword();
        role = user.getRole();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(final String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(final UserRole role) {
        this.role = role;
    }
}
