package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;
import javax.persistence.*;
import java.util.Objects;

@Entity
public class Customer {

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

    @Column(nullable = false,unique = true)
    @Embedded
    private UserData login;

    public Customer(){}





}
