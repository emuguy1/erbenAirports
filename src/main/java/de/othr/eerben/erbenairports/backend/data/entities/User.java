package de.othr.eerben.erbenairports.backend.data.entities;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User extends SingleIdEntity<String> implements UserDetails {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    @Nullable
    private String name;

    @Column
    @Nullable
    private String surname;

    @Column
    @Nullable
    private String iban;

    @Column
    @Nullable
    private String country;

    @Column
    @Nullable
    private int zipCode;

    @Column
    @Nullable
    private String town;

    @Column
    @Nullable
    private String street;

    @Column
    @Nullable
    private String housenumber;

    @Column
    @Nullable
    private String company_name;

    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;

    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public User(String username, String password, String name, String surname, String iban, String country, int zipCode, String town, String street, String housenumber, String company_name, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.iban = iban;
        this.country = country;
        this.zipCode = zipCode;
        this.town = town;
        this.street = street;
        this.housenumber = housenumber;
        this.company_name = company_name;
        this.accountType = accountType;
    }

    public User(String username, String password, String name, String surname, String country, int zipCode, String town, String street, String housenumber, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.zipCode = zipCode;
        this.town = town;
        this.street = street;
        this.housenumber = housenumber;
        this.accountType = accountType;
    }

    public User(String username, String password, String name, String surname, String iban, String country, int zipCode, String town, String street, String housenumber, String company_name) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.iban = iban;
        this.country = country;
        this.zipCode = zipCode;
        this.town = town;
        this.street = street;
        this.housenumber = housenumber;
        this.company_name = company_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public String getID() {
        return this.username;
    }
}
