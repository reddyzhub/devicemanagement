package com.example.devicemanagement.controller;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.exception.DeviceNotFoundException;
import com.example.devicemanagement.service.DeviceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DeviceController.
 */
class DeviceControllerTest {

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private DeviceController deviceController;

    private AutoCloseable closeable;
    /**
     * Initializes mocks before each test.
     */

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    /**
     * Closes mocks after each test.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Test for adding a device.
     * Ensures that the device is created and returned correctly.
     */

    @Test
    void addDevice_shouldCreateDevice() {
        // Arrange
        DeviceDTO deviceDTO = new DeviceDTO(null, "Device1", "BrandA", LocalDateTime.now());
        DeviceDTO createdDeviceDTO = new DeviceDTO(1L, "Device1", "BrandA", LocalDateTime.now());
        when(deviceService.addDevice(any(DeviceDTO.class))).thenReturn(createdDeviceDTO);

        // Act
        ResponseEntity<DeviceDTO> responseEntity = deviceController.addDevice(deviceDTO);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(201);
        assertThat(responseEntity.getBody()).isEqualTo(createdDeviceDTO);
        verify(deviceService, times(1)).addDevice(any(DeviceDTO.class));
    }
    /**
     * Test for retrieving a device by ID.
     * Ensures that the device is returned correctly if it exists.
     */

    @Test
    void getDeviceById_shouldReturnDeviceIfExists() {
        // Arrange
        Long id = 1L;
        DeviceDTO deviceDTO = new DeviceDTO(id, "Device1", "BrandA", LocalDateTime.now());
        when(deviceService.getDeviceById(id)).thenReturn(deviceDTO);

        // Act
        ResponseEntity<DeviceDTO> responseEntity = deviceController.getDeviceById(id);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(deviceDTO);
        verify(deviceService, times(1)).getDeviceById(id);
    }

    /**
     * Test for retrieving a device by ID when it does not exist.
     * Ensures that a 404 status is returned.
     */

    @Test
    void getDeviceById_shouldReturnNotFoundIfDeviceDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(deviceService.getDeviceById(id)).thenThrow(new DeviceNotFoundException(id));

        // Act
        ResponseEntity<DeviceDTO> responseEntity = deviceController.getDeviceById(id);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        verify(deviceService, times(1)).getDeviceById(id);
    }
    /**
     * Test for retrieving all devices.
     * Ensures that all devices are returned correctly.
     */

    @Test
    void getAllDevices_shouldReturnAllDevices() {
        // Arrange
        List<DeviceDTO> devices = Arrays.asList(
                new DeviceDTO(1L, "Device1", "BrandA", LocalDateTime.now()),
                new DeviceDTO(2L, "Device2", "BrandB", LocalDateTime.now())
        );
        when(deviceService.getAllDevices()).thenReturn(devices);

        // Act
        ResponseEntity<List<DeviceDTO>> responseEntity = deviceController.getAllDevices();

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(devices);
        verify(deviceService, times(1)).getAllDevices();
    }
    /**
     * Test for updating a device.
     * Ensures that the device is updated correctly if it exists.
     */

    @Test
    void updateDevice_shouldUpdateDeviceIfExists() {
        // Arrange
        Long id = 1L;
        DeviceDTO updatedDeviceDTO = new DeviceDTO(id, "Device1 Updated", "BrandA", LocalDateTime.now());
        when(deviceService.updateDevice(eq(id), any(DeviceDTO.class))).thenReturn(updatedDeviceDTO);

        // Act
        ResponseEntity<DeviceDTO> responseEntity = deviceController.updateDevice(id, updatedDeviceDTO);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(updatedDeviceDTO);
        verify(deviceService, times(1)).updateDevice(eq(id), any(DeviceDTO.class));
    }
    /**
     * Test for updating a device when it does not exist.
     * Ensures that a 404 status is returned.
     */

    @Test
    void updateDevice_shouldReturnNotFoundIfDeviceDoesNotExist() {
        // Arrange
        Long id = 1L;
        DeviceDTO updatedDeviceDTO = new DeviceDTO(id, "Device1 Updated", "BrandA", LocalDateTime.now());
        when(deviceService.updateDevice(eq(id), any(DeviceDTO.class))).thenThrow(new DeviceNotFoundException(id));

        // Act
        ResponseEntity<DeviceDTO> responseEntity = deviceController.updateDevice(id, updatedDeviceDTO);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        verify(deviceService, times(1)).updateDevice(eq(id), any(DeviceDTO.class));
    }
    /**
     * Test for partially updating a device.
     * Ensures that the device is updated correctly if it exists.
     */

    @Test
    void updateDevicePartially_shouldUpdateDeviceIfExists() {
        // Arrange
        Long id = 1L;
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Device1 Updated");
        fields.put("brand", "BrandA");
        DeviceDTO updatedDeviceDTO = new DeviceDTO(id, "Device1 Updated", "BrandA", LocalDateTime.now());
        when(deviceService.updateDevicePartially(eq(id), any(Map.class))).thenReturn(updatedDeviceDTO);

        // Act
        ResponseEntity<DeviceDTO> responseEntity = deviceController.updateDevicePartially(id, fields);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(updatedDeviceDTO);
        verify(deviceService, times(1)).updateDevicePartially(eq(id), any(Map.class));
    }
    /**
     * Test for partially updating a device when it does not exist.
     * Ensures that a 404 status is returned.
     */

    @Test
    void updateDevicePartially_shouldReturnNotFoundIfDeviceDoesNotExist() {
        // Arrange
        Long id = 1L;
        Map<String, Object> fields = new HashMap<>();
        when(deviceService.updateDevicePartially(eq(id), any(Map.class))).thenThrow(new DeviceNotFoundException(id));

        // Act
        ResponseEntity<DeviceDTO> responseEntity = deviceController.updateDevicePartially(id, fields);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        verify(deviceService, times(1)).updateDevicePartially(eq(id), any(Map.class));
    }
    /**
     * Test for deleting a device.
     * Ensures that the device is deleted correctly if it exists.
     */

    @Test
    void deleteDevice_shouldDeleteDeviceIfExists() {
        // Arrange
        Long id = 1L;
        doNothing().when(deviceService).deleteDevice(id);

        // Act
        ResponseEntity<Void> responseEntity = deviceController.deleteDevice(id);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
        verify(deviceService, times(1)).deleteDevice(id);
    }
    /**
     * Test for deleting a device when it does not exist.
     * Ensures that a 404 status is returned.
     */

    @Test
    void deleteDevice_shouldReturnNotFoundIfDeviceDoesNotExist() {
        // Arrange
        Long id = 1L;
        doThrow(new DeviceNotFoundException(id)).when(deviceService).deleteDevice(id);

        // Act
        ResponseEntity<Void> responseEntity = deviceController.deleteDevice(id);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        verify(deviceService, times(1)).deleteDevice(id);
    }
    /**
     * Test for searching devices by brand.
     * Ensures that the correct devices are returned if found.
     */

    @Test
    void searchDevicesByBrand_shouldReturnDevicesIfFound() {
        // Arrange
        String brand = "BrandA";
        List<DeviceDTO> devices = Arrays.asList(
                new DeviceDTO(1L, "Device1", "BrandA", LocalDateTime.now()),
                new DeviceDTO(2L, "Device2", "BrandA", LocalDateTime.now())
        );
        when(deviceService.searchDevicesByBrand(brand)).thenReturn(devices);

        // Act
        ResponseEntity<List<DeviceDTO>> responseEntity = deviceController.searchDevicesByBrand(brand);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(devices);
        verify(deviceService, times(1)).searchDevicesByBrand(brand);
    }
    /**
     * Test for searching devices by brand when no devices are found.
     * Ensures that a 404 status is returned.
     */

    @Test
    void searchDevicesByBrand_shouldReturnNotFoundIfNoDevicesFound() {
        // Arrange
        String brand = "BrandA";
        when(deviceService.searchDevicesByBrand(brand)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<DeviceDTO>> responseEntity = deviceController.searchDevicesByBrand(brand);

        // Assert
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        verify(deviceService, times(1)).searchDevicesByBrand(brand);
    }
}
