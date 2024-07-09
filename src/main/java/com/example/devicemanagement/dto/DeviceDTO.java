package com.example.devicemanagement.dto;


import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Map;

public record DeviceDTO(
        Long id,
        @NotEmpty(message = "Name is mandatory") String name,
        @NotEmpty(message = "Brand is mandatory") String brand,
        LocalDateTime creationTime) {

    public DeviceDTO withUpdates(Map<String, Object> updates) {
        String newName = updates.getOrDefault("name", name) != null ? (String) updates.get("name") : name;
        String newBrand = updates.getOrDefault("brand", brand) != null ? (String) updates.get("brand") : brand;
        LocalDateTime newCreationTime = updates.getOrDefault("creationTime", creationTime) != null ? (LocalDateTime) updates.get("creationTime") : creationTime;
        return new DeviceDTO(id, newName, newBrand, newCreationTime);
    }
}
