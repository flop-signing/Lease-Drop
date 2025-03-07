package com.bedatasolutions.leaseDrop.dto;

public record SubscriptionDto(
        Integer id,
        Integer version,
        String planType,
        Integer documentLimit,
        String stripeCustomerId,
        String stripeSubscriptionId,
        String status,
        Integer userId
) {



}
