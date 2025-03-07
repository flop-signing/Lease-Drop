package com.bedatasolutions.leaseDrop.dto;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.RolesDao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PermissionDto(

        Integer id,
        Integer version,

        @Size(min = 3, max = 10, message = "Invalid input : name")
        @Schema(title = "Permission name", example = "PERMISSION_WRITE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String name,

//        @NotEmpty(message = "Empty field")
//        @NotNull(message = "Null entry")
//        @Size(min = 5, message = "Minimum 5")
//        @Schema(title = "Permission description", example = "this is permission description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String description
    //    Set<RolesDao> roles
        ) {
}
