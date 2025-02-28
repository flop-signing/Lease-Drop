package com.bedatasolutions.leaseDrop.dto;


public record DropdownItemDto(
        Integer id,
        Integer version,
        String bgColor,
        String description,
        String name,
        Integer sortOrder,
        String textColor
       // Integer dropdownTypeId, // Assuming DropdownTypeDao has an ID field
        //Integer parentId // Nullable, assuming DropdownItemDao has an ID field for parent
) {


}
