package com.example.devicemanagement.service;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.exception.DeviceNotFoundException;
import com.example.devicemanagement.exception.DeviceServiceException;
import com.example.devicemanagement.model.Device;
import com.example.devicemanagement.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing devices.
 */
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /**
     * Adds a new device.
     * @param deviceDTO Data transfer object containing device details.
     * @return The saved device data.
     */
    @Transactional // Ensures atomicity and consistency of the add operation.
    public DeviceDTO addDevice(DeviceDTO deviceDTO) {
        try {
            Device device = Device.builder()
                    .name(deviceDTO.name())
                    .brand(deviceDTO.brand())
                    .creationTime(deviceDTO.creationTime() != null ? deviceDTO.creationTime() : LocalDateTime.now())
                    .build();
            device = deviceRepository.save(device);
            return new DeviceDTO(device.getId(), device.getName(), device.getBrand(), device.getCreationTime());
        } catch (Exception e) {
            throw new DeviceServiceException("Error adding device", e);
        }
    }

    /**
     * Retrieves a device by its ID.
     * @param id The ID of the device.
     * @return The device data.
     */
    public DeviceDTO getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .map(d -> new DeviceDTO(d.getId(), d.getName(), d.getBrand(), d.getCreationTime()))
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    /**
     * Retrieves all devices.
     * @return A list of all devices.
     */
    public List<DeviceDTO> getAllDevices() {
        try {
            return deviceRepository.findAll().stream()
                    .map(d -> new DeviceDTO(d.getId(), d.getName(), d.getBrand(), d.getCreationTime()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DeviceServiceException("Error retrieving devices", e);
        }
    }
    /**
     * Updates an existing device.
     * @param id The ID of the device to update.
     * @param updatedDeviceDTO Data transfer object containing updated device details.
     * @return The updated device data.
     */
    @Transactional // Ensures atomicity and consistency of the update operation.
    public DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        device.setName(updatedDeviceDTO.name());
        device.setBrand(updatedDeviceDTO.brand());
        device.setCreationTime(updatedDeviceDTO.creationTime() != null ? updatedDeviceDTO.creationTime() : LocalDateTime.now());
        device = deviceRepository.save(device);
        return new DeviceDTO(device.getId(), device.getName(), device.getBrand(), device.getCreationTime());
    }
    /**
     * Partially updates an existing device.
     * @param id The ID of the device to update.
     * @param updates A map containing the fields to update.
     * @return The updated device data.
     */
    @Transactional // Ensures atomicity and consistency of the partial update operation.
    public DeviceDTO updateDevicePartially(Long id, Map<String, Object> updates) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> device.setName((String) value);
                case "brand" -> device.setBrand((String) value);
                case "creationTime" -> device.setCreationTime((LocalDateTime) value);
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        Device savedDevice = deviceRepository.save(device);
        return new DeviceDTO(savedDevice.getId(), savedDevice.getName(), savedDevice.getBrand(), savedDevice.getCreationTime());
    }
    /**
     * Deletes a device by its ID.
     * @param id The ID of the device to delete.
     */
    @Transactional // Ensures atomicity and consistency of the delete operation.
    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException(id);
        }
        deviceRepository.deleteById(id);
    }
    /**
     * Searches devices by brand.
     * @param brand The brand of the devices to search for.
     * @return A list of devices with the specified brand.
     */
    public List<DeviceDTO> searchDevicesByBrand(String brand) {
        try {
            return deviceRepository.findByBrand(brand).stream()
                    .map(d -> new DeviceDTO(d.getId(), d.getName(), d.getBrand(), d.getCreationTime()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DeviceServiceException("Error finding devices by brand", e);
        }
    }
}
