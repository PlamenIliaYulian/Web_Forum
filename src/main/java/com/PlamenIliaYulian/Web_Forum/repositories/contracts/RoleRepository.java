package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Role;

import java.util.List;

public interface RoleRepository {
    Role getRoleById(int id);

    Role getRoleByName(String name);

    List<Role> getAllRoles();

}
