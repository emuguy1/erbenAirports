package de.othr.eerben.erbenairports.backend.data.entities;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserData {

    private String username;

    private String password;

    public UserData(){};

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return username.equals(userData.username) && password.equals(userData.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
