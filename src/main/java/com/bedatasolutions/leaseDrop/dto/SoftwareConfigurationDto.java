package com.bedatasolutions.leaseDrop.dto;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;

public record SoftwareConfigurationDto(
        Integer id,
        Integer version,
    //    ActionType actionType,
//        Integer userMod,
//        Integer userAdded,
        String configKey,
        String configValue) {
}
