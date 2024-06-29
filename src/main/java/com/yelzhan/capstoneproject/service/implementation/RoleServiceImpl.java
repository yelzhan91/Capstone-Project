package com.yelzhan.capstoneproject.service.implementation;

import com.yelzhan.capstoneproject.model.entity.Role;
import com.yelzhan.capstoneproject.repository.RoleRepository;
import com.yelzhan.capstoneproject.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> fetchByRoleNames(String... roleNames) {
        return roleRepository.findAllByRoleIn(roleNames);
    }

}
