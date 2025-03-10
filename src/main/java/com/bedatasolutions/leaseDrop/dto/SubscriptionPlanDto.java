package com.bedatasolutions.leaseDrop.dto;

import java.math.BigDecimal;

public record SubscriptionPlanDto(
        Integer id,
        Integer version,
        String name,
        String owner,
        Integer docstatus,
        Integer idx,
        String planName,
        String currency,
        String item,
        String priceDetermination,
        BigDecimal cost,
        String priceList,
        String billingInterval,
        Integer billingIntervalCount,
        String productPriceId,
        String paymentGateway,
        String costCenter,
        String userTags,
        String comments,
        String assign,
        String likedBy
  //      String subscriptionPlanDetails
) {
}
