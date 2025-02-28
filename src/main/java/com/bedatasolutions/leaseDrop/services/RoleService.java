package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.dao.RolesDao;
import com.bedatasolutions.leaseDrop.dto.RoleDto;
import com.bedatasolutions.leaseDrop.repo.PermissionRepo;
import com.bedatasolutions.leaseDrop.repo.RolesRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

   // private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private final RolesRepo rolesRepo;

    public RoleService(RolesRepo rolesRepo) {
        this.rolesRepo = rolesRepo;
    }

    // Create Role
    public RoleDto create(RoleDto roleDto) {
        RolesDao rolesDao = dtoToDao(roleDto,new RolesDao());
        RolesDao savedRole = rolesRepo.save(rolesDao);
        return daoToDto(savedRole);
    }

    // Get All Roles
    @Transactional
    public List<RoleDto> getAllRoles() {
        List<RolesDao> roles = rolesRepo.findAll();
        return roles.stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());
    }

    // Get Role by ID
    @Transactional
    public RoleDto getRoleById(Integer id) {
        RolesDao rolesDao = rolesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return daoToDto(rolesDao);
    }

    @Transactional
    public RoleDto update(RoleDto roleDto) {
        // Find existing role by ID
        RolesDao existingRoleOpt = rolesRepo.getReferenceById(roleDto.id());
        if (existingRoleOpt.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + roleDto.id());
        }


        // Save updated role with optimistic locking support
        RolesDao updatedRole = rolesRepo.save(dtoToDao(roleDto, existingRoleOpt));  // Hibernate handles version checking automatically

        // Return the updated RoleDto
        return daoToDto(updatedRole);
    }



    // Delete Role
    @Transactional
    public void delete(Integer id) {
        Optional<RolesDao> existingRole = rolesRepo.findById(id);
        if (existingRole.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + id);
        }
        rolesRepo.deleteById(id);
    }

    // Get Roles with Pagination
    public Page<RoleDto> getRolesWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RolesDao> rolePage = rolesRepo.findAll(pageable);
        return rolePage.map(this::daoToDto);
    }


    public RoleDto daoToDto(RolesDao rolesDao) {
        return new RoleDto(
                rolesDao.getId(),
                rolesDao.getVersion(),
//                rolesDao.getActionType(),
//                rolesDao.getUserMod(),
//                rolesDao.getUserAdded(),
                rolesDao.getName(),
                rolesDao.getDescription(),
                rolesDao.getPermissions(),
                rolesDao.getUsers()
        );
    }

    public RolesDao dtoToDao(RoleDto roleDto, RolesDao rolesDao) {

//        permissionDao.setActionKey(ActionType.UPDATE);
//        permissionDao.setActionType(ActionType.UPDATE);


        rolesDao.setId(roleDto.id());
        rolesDao.setVersion(roleDto.version());
//        rolesDao.setActionType(roleDto.actionType());
//        rolesDao.setUserMod(roleDto.userMod());
//        rolesDao.setUserAdded(roleDto.userAdded());
        rolesDao.setName(roleDto.name());
        rolesDao.setDescription(roleDto.description());
        rolesDao.setPermissions(roleDto.permissions());
        rolesDao.setUsers(roleDto.users());
//        permissionDao.setDescription(permissionDto.description() != null ? permissionDto.description() : permissionDao.getDescription());
        return rolesDao;
    }

}
