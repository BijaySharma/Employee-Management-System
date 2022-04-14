package com.bijay.ems.model;

import javax.persistence.*;

@Entity
@Table(name="EMPLOYEE")
public class Employee {
    @Id
    @Column(name="EMP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="DEPT", nullable = false)
    private String department;

    @Column(name="DESG", nullable = false)
    private String designation;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="phone", unique = true)
    private String phone;

    public Employee() {
    }


    public Employee(long id, String name, String department, String designation, String email, String phone) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.email = email.toLowerCase();
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "[" + "id=" + id + ", " + "name=" + name + ", " + "department=" + department + ", " + "designation=" + designation + ", "
                + "email=" + email + ", " + "Phone" + phone +
                "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
