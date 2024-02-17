package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Role;

import java.util.List;

public interface RoleService {
    Role getRoleById(int id);
    Role getRoleByName(String name);
    List<Role> getAllRoles();
}
