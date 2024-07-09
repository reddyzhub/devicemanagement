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

@RestController
@RequestMapping("/devices")
@Tag(name = "Device Management", description = "APIs for managing devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

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
