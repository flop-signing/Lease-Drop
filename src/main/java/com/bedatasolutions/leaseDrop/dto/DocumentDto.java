package com.bedatasolutions.leaseDrop.dto;

import java.util.List;

public record DocumentDto(
        Integer id,
        Integer version,
        String filePath,
        Integer userId
     //   List<DocumentSummaryDto> documentSummary

)

       // Integer dropdownItem2,
        //Integer dropdownItem3)
{

}
