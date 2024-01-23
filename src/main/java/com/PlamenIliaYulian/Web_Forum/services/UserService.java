package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.User;

public interface UserService {
    User getByUsername(String username);
}
