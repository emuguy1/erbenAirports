package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User extends SingleIdEntity<Long> implements UserDetails {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerId;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private int zipCode;

    @Column(nullable = false)
    private String town;

    @Column(nullable = false)
    private String housenumber;

    @Column(nullable = false)
    private String company_name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Long getID() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
