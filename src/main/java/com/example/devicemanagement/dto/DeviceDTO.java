package com.example.devicemanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for Device.
 *
 * DTOs are used to transfer data between layers of an application.
 * They help decouple the service layer from the presentation layer,
 * enhance security by exposing only necessary data, and include validation.
 *
 * This class follows the Data Transfer Object (DTO) pattern.
 */
public record DeviceDTO(
        Long id,
        @NotEmpty(message = "Name is mandatory") String name,
        @NotEmpty(message = "Brand is mandatory") String brand,
        @NotNull(message = "Creation time is mandatory")
        @PastOrPresent(message = "Creation time must be in the past or present") LocalDateTime creationTime) {

    /**
     * Updates the fields of the DeviceDTO with the values from the provided map.
     * If a value is not present in the map, the existing value is retained.
     *
     * @param updates A map containing the fields to update.
     * @return A new DeviceDTO instance with the updated fields.
     */
    public DeviceDTO withUpdates(Map<String, Object> updates) {
        String newName = updates.getOrDefault("name", name) != null ? (String) updates.get("name") : name;
        String newBrand = updates.getOrDefault("brand", brand) != null ? (String) updates.get("brand") : brand;
        LocalDateTime newCreationTime = updates.getOrDefault("creationTime", creationTime) != null ? (LocalDateTime) updates.get("creationTime") : creationTime;
        return new DeviceDTO(id, newName, newBrand, newCreationTime);
    }
}
