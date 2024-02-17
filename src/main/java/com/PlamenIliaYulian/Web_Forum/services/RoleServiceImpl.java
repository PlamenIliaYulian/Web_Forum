package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.RoleRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleById(int id){
        return roleRepository.getRoleById(id);
    }

    @Override
    public Role getRoleByName(String name){
        return roleRepository.getRoleByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.getAllRoles();
    }


}
