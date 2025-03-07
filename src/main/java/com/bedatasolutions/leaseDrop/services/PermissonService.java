package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.dao.PermissionDao;
import com.bedatasolutions.leaseDrop.dto.PermissionDto;
import com.bedatasolutions.leaseDrop.repo.PermissionRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissonService {

    private final PermissionRepo permissionRepo;

    public PermissonService(PermissionRepo permissionRepo) {
        this.permissionRepo = permissionRepo;
    }

    // Create Permission
    @Transactional
    public PermissionDto create(PermissionDto permissionDto) {
        PermissionDao permissionDao = dtoToDao(permissionDto, new PermissionDao());
        PermissionDao savedPermission = permissionRepo.save(permissionDao);
        return daoToDto(savedPermission);
    }

    // Get All Permissions
    @Transactional
    public List<PermissionDto> getAllPermissions() {
        List<PermissionDao> permissions = permissionRepo.findAll();
        return permissions.stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());
    }

    // Get Permission by ID
    @Transactional
    public PermissionDto getPermissionById(Integer id) {
        PermissionDao permissionDao = permissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        return daoToDto(permissionDao);
    }

    // Update Existing Permission
    @Transactional
    public PermissionDto update(PermissionDto permissionDto) {
        // Find existing role by ID
        PermissionDao existingPermissionOpt = permissionRepo.getReferenceById(permissionDto.id());
        if (existingPermissionOpt.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found with id: " + permissionDto.id());
        }

        PermissionDao updatedPermission = permissionRepo.save(dtoToDao(permissionDto, existingPermissionOpt));  // Hibernate handles version checking automatically

        // Return the updated RoleDto
        return daoToDto(updatedPermission);
    }


//    @Transactional
    public PermissionDto daoToDto(PermissionDao permissionDao) {
        return new PermissionDto(
                permissionDao.getId(),
                permissionDao.getVersion(),
                permissionDao.getName(),
                permissionDao.getDescription()

        );
    }

    public PermissionDao dtoToDao(PermissionDto permissionDto, PermissionDao permissionDao) {

        permissionDao.setVersion(permissionDto.version());
        permissionDao.setName(permissionDto.name());
        permissionDao.setDescription(permissionDto.description());
        return permissionDao;
    }

    // Delete Role
    @Transactional
    public void delete(Integer id) {
        Optional<PermissionDao> existingPermission = permissionRepo.findById(id);
        if (existingPermission.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found with id: " + id);
        }
        permissionRepo.deleteById(id);
    }

}
