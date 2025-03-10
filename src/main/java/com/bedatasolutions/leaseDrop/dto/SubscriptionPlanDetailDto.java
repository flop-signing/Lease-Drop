package com.bedatasolutions.leaseDrop.dto;

public record SubscriptionPlanDetailDto(
        Integer id,
        Integer version,
        String name,
        String owner,
        Integer docstatus,
        Integer idx,
        String plan,
        Integer qty,
        String parent,
        String parentField,
        String parentType
       //  String subscriptionPlanName
) {
}
