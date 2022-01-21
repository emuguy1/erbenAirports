package de.othr.eerben.erbenairports.backend.data.entities;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class User extends SingleIdEntity<String> implements UserDetails {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Name cannot be empty!")
    private String name;

    @NotBlank(message = "Surname cannot be empty!")
    private String surname;

    @Nullable
    private String iban;

    @NotBlank(message = "Country cannot be empty!")
    private String country;

    @NotBlank(message = "ZIP-Code cannot be empty!")
    private String zipCode;

    @NotBlank(message = "City cannot be empty!")
    private String town;

    @NotBlank(message = "Street cannot be empty!")
    private String street;

    @NotBlank(message = "Housenumber cannot be empty!")
    private String housenumber;

    @Nullable
    private String company_name;

    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    //Customer
    public User(String username, String password, String name, String surname, String iban, String country, String zipCode, String town, String street, String housenumber, String company_name, AccountType accountType) {
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

    //Employee
    public User(String username, String password, String name, String surname, String country, String zipCode, String town, String street, String housenumber, AccountType accountType) {
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
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
        return List.of(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return User.this.accountType.name();
            }
        });
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

    public boolean isCustomer() {
        return this.accountType.equals(AccountType.CUSTOMER);
    }

    public boolean isEmployee() {
        return this.accountType.equals(AccountType.EMPLOYEE);
    }

    @Override
    public String getID() {
        return this.username;
    }

    public void setID(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", iban='" + iban + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                ", housenumber='" + housenumber + '\'' +
                ", company_name='" + company_name + '\'' +
                ", accountType=" + accountType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername()) && getPassword().equals(user.getPassword()) && getName().equals(user.getName()) && getSurname().equals(user.getSurname()) && Objects.equals(getIban(), user.getIban()) && getCountry().equals(user.getCountry()) && getZipCode().equals(user.getZipCode()) && getTown().equals(user.getTown()) && getStreet().equals(user.getStreet()) && getHousenumber().equals(user.getHousenumber()) && Objects.equals(getCompany_name(), user.getCompany_name()) && getAccountType() == user.getAccountType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUsername(), getPassword(), getName(), getSurname(), getIban(), getCountry(), getZipCode(), getTown(), getStreet(), getHousenumber(), getCompany_name(), getAccountType());
    }
}
