package com.example.devicemanagement.service;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.exception.DeviceNotFoundException;
import com.example.devicemanagement.exception.DeviceServiceException;
import com.example.devicemanagement.model.Device;
import com.example.devicemanagement.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeviceServiceTest implements AutoCloseable {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void addDevice_shouldSaveDevice() {
        // Arrange
        DeviceDTO deviceDTO = new DeviceDTO(null, "Device1", "BrandA", LocalDateTime.now());
        Device device = Device.builder()
                .id(1L)
                .name("Device1")
                .brand("BrandA")
                .creationTime(LocalDateTime.now())
                .build();

        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        // Act
        DeviceDTO savedDevice = deviceService.addDevice(deviceDTO);

        // Assert
        assertThat(savedDevice).isNotNull();
        assertThat(savedDevice.name()).isEqualTo("Device1");
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void getDeviceById_shouldReturnDeviceIfExists() {
        // Arrange
        Long id = 1L;
        Device device = new Device(id, "Device1", "BrandA", LocalDateTime.now());
        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        // Act
        DeviceDTO foundDevice = deviceService.getDeviceById(id);

        // Assert
        assertThat(foundDevice).isNotNull();
        assertThat(foundDevice.id()).isEqualTo(id);
        verify(deviceRepository, times(1)).findById(id);
    }

    @Test
    void getDeviceById_shouldThrowExceptionIfDeviceNotFound() {
        // Arrange
        Long id = 1L;
        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deviceService.getDeviceById(id))
                .isInstanceOf(DeviceNotFoundException.class);
        verify(deviceRepository, times(1)).findById(id);
    }

    @Test
    void getAllDevices_shouldReturnAllDevices() {
        // Arrange
        List<Device> devices = Arrays.asList(
                new Device(1L, "Device1", "BrandA", LocalDateTime.now()),
                new Device(2L, "Device2", "BrandB", LocalDateTime.now())
        );
        when(deviceRepository.findAll()).thenReturn(devices);

        // Act
        List<DeviceDTO> deviceDTOs = deviceService.getAllDevices();

        // Assert
        assertThat(deviceDTOs).hasSize(2);
        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void updateDevice_shouldUpdateDeviceIfExists() {
        // Arrange
        Long id = 1L;
        Device existingDevice = new Device(id, "Device1", "BrandA", LocalDateTime.now());
        Device updatedDevice = new Device(id, "Device1 Updated", "BrandA", LocalDateTime.now());
        DeviceDTO updatedDeviceDTO = new DeviceDTO(id, "Device1 Updated", "BrandA", LocalDateTime.now());

        when(deviceRepository.findById(id)).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(updatedDevice);

        // Act
        DeviceDTO result = deviceService.updateDevice(id, updatedDeviceDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Device1 Updated");
        verify(deviceRepository, times(1)).findById(id);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void updateDevice_shouldThrowExceptionIfDeviceNotFound() {
        // Arrange
        Long id = 1L;
        DeviceDTO updatedDeviceDTO = new DeviceDTO(id, "Device1 Updated", "BrandA", LocalDateTime.now());
        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deviceService.updateDevice(id, updatedDeviceDTO))
                .isInstanceOf(DeviceNotFoundException.class);
        verify(deviceRepository, times(1)).findById(id);
    }

    @Test
    void deleteDevice_shouldDeleteDeviceIfExists() {
        // Arrange
        Long id = 1L;
        when(deviceRepository.existsById(id)).thenReturn(true);

        // Act
        deviceService.deleteDevice(id);

        // Assert
        verify(deviceRepository, times(1)).existsById(id);
        verify(deviceRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteDevice_shouldThrowExceptionIfDeviceNotFound() {
        // Arrange
        Long id = 1L;
        when(deviceRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.deleteDevice(id))
                .isInstanceOf(DeviceNotFoundException.class);
        verify(deviceRepository, times(1)).existsById(id);
        verify(deviceRepository, never()).deleteById(id);
    }

    @Test
    void searchDevicesByBrand_shouldReturnMatchingDevices() {
        // Arrange
        String brand = "BrandA";
        List<Device> devices = Arrays.asList(
                new Device(1L, "Device1", "BrandA", LocalDateTime.now()),
                new Device(2L, "Device2", "BrandA", LocalDateTime.now())
        );
        when(deviceRepository.findByBrand(brand)).thenReturn(devices);

        // Act
        List<DeviceDTO> deviceDTOs = deviceService.searchDevicesByBrand(brand);

        // Assert
        assertThat(deviceDTOs).hasSize(2);
        verify(deviceRepository, times(1)).findByBrand(brand);
    }

    @Override
    public void close() throws Exception {
        mocks.close();
    }
}
