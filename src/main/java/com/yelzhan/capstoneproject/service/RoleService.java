package com.yelzhan.capstoneproject.service;

import com.yelzhan.capstoneproject.model.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> fetchByRoleNames(String... roleNames);

}
