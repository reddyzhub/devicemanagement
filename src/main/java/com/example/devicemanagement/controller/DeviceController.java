package com.example.devicemanagement.controller;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.exception.DeviceNotFoundException;
import com.example.devicemanagement.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing devices.
 * Uses Swagger annotations for API documentation.
 */
@RestController
@RequestMapping("/devices")
@Tag(name = "Device Management", description = "APIs for managing devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    /**
     * Adds a new device.
     * @param deviceDTO Data transfer object containing device details.
     * @return The created device.
     */
    @PostMapping
    @Operation(summary = "Add a new device", description = "Creates a new device and returns the created device.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Error adding device")
    })
    public ResponseEntity<DeviceDTO> addDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        DeviceDTO createdDevice = deviceService.addDevice(deviceDTO);
        return ResponseEntity.status(201).body(createdDevice);
    }
    /**
     * Retrieves a device by its ID.
     * @param id The ID of the device.
     * @return The device data.
     */

    @GetMapping("/{id}")
    @Operation(summary = "Get a device by ID", description = "Retrieves a device by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving device by ID")
    })
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable("id") Long id) {
        try {
            DeviceDTO deviceDTO = deviceService.getDeviceById(id);
            return ResponseEntity.ok(deviceDTO);
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    /**
     * Retrieves all devices.
     * @return A list of all devices.
     */

    @GetMapping
    @Operation(summary = "Get all devices", description = "Retrieves all devices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Error retrieving devices")
    })
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<DeviceDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    /**
     * Updates an existing device.
     * @param id The ID of the device to update.
     * @param updatedDeviceDTO Data transfer object containing updated device details.
     * @return The updated device data.
     */

    @PutMapping("/{id}")
    @Operation(summary = "Update a device", description = "Updates an existing device with new data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device updated successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "500", description = "Error updating device")
    })
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("id") Long id, @Valid @RequestBody DeviceDTO updatedDeviceDTO) {
        try {
            DeviceDTO deviceDTO = deviceService.updateDevice(id, updatedDeviceDTO);
            return ResponseEntity.ok(deviceDTO);
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Partially updates an existing device.
     * @param id The ID of the device to update.
     * @param fields A map containing the fields to update.
     * @return The updated device data.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a device", description = "Partially updates an existing device with new data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device partially updated successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "500", description = "Error partially updating device")
    })
    public ResponseEntity<DeviceDTO> updateDevicePartially(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields) {
        try {
            DeviceDTO updatedDeviceDTO = deviceService.updateDevicePartially(id, fields);
            return ResponseEntity.ok(updatedDeviceDTO);
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    /**
     * Deletes a device by its ID.
     * @param id The ID of the device to delete.
     * @return Response entity with status.
     */

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device", description = "Deletes a device by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "500", description = "Error deleting device")
    })
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") Long id) {
        try {
            deviceService.deleteDevice(id);
            return ResponseEntity.noContent().build();
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
    /**
     * Searches devices by brand.
     * @param brand The brand of the devices to search for.
     * @return A list of devices with the specified brand.
     */

    @GetMapping("/search/brand/{brand}")
    @Operation(summary = "Search devices by brand", description = "Searches for devices by their brand.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Devices not found"),
            @ApiResponse(responseCode = "500", description = "Error searching devices by brand")
    })
    public ResponseEntity<List<DeviceDTO>> searchDevicesByBrand(@PathVariable("brand") String brand) {
        List<DeviceDTO> devices = deviceService.searchDevicesByBrand(brand);
        if (devices.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(devices);
    }
}
