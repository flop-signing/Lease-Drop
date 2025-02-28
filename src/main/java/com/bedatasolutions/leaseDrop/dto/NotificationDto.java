package com.bedatasolutions.leaseDrop.dto;


public record NotificationDto(

        Integer id,
        Integer version,
        String content,
        String channel,
        String subject,
        String sender,
        String senderEmail
        //Integer userId, // Assuming UserDao has an ID field
        //Integer notificationTypeId, // Assuming DropdownItemDao has an ID field
        //Integer statusId // Assuming DropdownItemDao has an ID field
){

}
