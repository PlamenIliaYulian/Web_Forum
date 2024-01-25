package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Role;

public interface RoleRepository {
    Role getRoleById(int id);

    Role getRoleByName(String name);

}
