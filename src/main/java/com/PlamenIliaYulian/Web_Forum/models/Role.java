package com.PlamenIliaYulian.Web_Forum.models;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role implements Comparable<Role> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "name")
    private String name;

    public Role() {
    }

    public Role(int roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return roleId == role.roleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }

    /* TODO - Пламка, Илийка - тази имплементация ни трябва, за да могат да минават тестовете.*/
    @Override
    public int compareTo(Role o) {
        return Integer.compare(this.roleId, o.roleId);
    }
}
