package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Role;

public interface RoleService {
    Role getRoleById(int id);

    Role getRoleByName(String name);
}
