package com.bedatasolutions.leaseDrop.dto;

import com.bedatasolutions.leaseDrop.dao.PermissionDao;
import com.bedatasolutions.leaseDrop.dao.UserDao;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RoleDto(
        Integer id,
        Integer version,
//        ActionType actionType,
//        Integer userMod,
//        Integer userAdded,
        @NotNull(message = "Role name is required")
        @NotEmpty(message = "Role name cannot be empty")
        String name,
        String description,
        Set<PermissionDao> permissions,
        Set<UserDao> users) {
}
