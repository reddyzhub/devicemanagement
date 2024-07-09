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

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Transactional
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

    public DeviceDTO getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .map(d -> new DeviceDTO(d.getId(), d.getName(), d.getBrand(), d.getCreationTime()))
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public List<DeviceDTO> getAllDevices() {
        try {
            return deviceRepository.findAll().stream()
                    .map(d -> new DeviceDTO(d.getId(), d.getName(), d.getBrand(), d.getCreationTime()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DeviceServiceException("Error retrieving devices", e);
        }
    }

    @Transactional
    public DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        device.setName(updatedDeviceDTO.name());
        device.setBrand(updatedDeviceDTO.brand());
        device.setCreationTime(updatedDeviceDTO.creationTime() != null ? updatedDeviceDTO.creationTime() : LocalDateTime.now());
        device = deviceRepository.save(device);
        return new DeviceDTO(device.getId(), device.getName(), device.getBrand(), device.getCreationTime());
    }

    @Transactional
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

    @Transactional
    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException(id);
        }
        deviceRepository.deleteById(id);
    }

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
