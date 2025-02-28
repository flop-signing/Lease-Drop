package com.bedatasolutions.leaseDrop.dto;


public record DropdownTypeDto(

        Integer id,
        Integer version,
        String description,
        String name,
        Integer sortOrder
        //Integer parentId, // Nullable, assuming DropdownTypeDao has an ID field for parent
       // Integer childId // Nullable, assuming DropdownTypeDao has an ID field for child
) {

}
