package com.bedatasolutions.leaseDrop.dto;

public record AdminSettingDto(
        Integer id,
        Integer version,
        String settingsKey,
        String settingValue,
        Boolean emailNotifications,
        Integer tempFileDuration,
        Integer cacheDuration,
        Integer subscriptionRemainderDuration,
        Boolean welcomeMail,
        Integer otpExpiry) {
}


