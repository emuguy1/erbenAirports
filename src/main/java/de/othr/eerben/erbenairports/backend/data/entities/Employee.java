package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Employee {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long employeeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false,unique = true)
    @Embedded
    private UserData login;


    public Employee(){}

    public Employee(String name,String surname,UserData login){
        this.name=name;
        this.surname=surname;
        this.login=login;
    }

}
