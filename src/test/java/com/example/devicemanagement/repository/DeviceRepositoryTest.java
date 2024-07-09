package com.example.devicemanagement.repository;

import com.example.devicemanagement.model.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Test class for DeviceRepository.
 * Uses DataJpaTest for configuring JPA tests and ActiveProfiles to set the test profile.
 */
@DataJpaTest
@ActiveProfiles("test")
public class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;
    /**
     * Sets up the test data before each test.
     * Clears the repository and adds some initial devices.
     */
    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll();

        Device device1 = Device.builder()
                .name("Device1")
                .brand("BrandA")
                .creationTime(LocalDateTime.now())
                .build();

        Device device2 = Device.builder()
                .name("Device2")
                .brand("BrandA")
                .creationTime(LocalDateTime.now())
                .build();

        Device device3 = Device.builder()
                .name("Device3")
                .brand("BrandB")
                .creationTime(LocalDateTime.now())
                .build();

        deviceRepository.save(device1);
        deviceRepository.save(device2);
        deviceRepository.save(device3);
    }
    /**
     * Tests finding devices by brand.
     * Ensures the correct number of devices are returned and their names match.
     */

    @Test
    void whenFindByBrand_thenReturnDevices() {
        List<Device> devices = deviceRepository.findByBrand("BrandA");

        assertThat(devices).hasSize(2);
        assertThat(devices).extracting(Device::getName).containsExactlyInAnyOrder("Device1", "Device2");
    }

    /**
     * Tests finding a device by ID.
     * Ensures the device is found and its details match.
     */

    @Test
    void whenFindById_thenReturnDevice() {
        Device device = Device.builder()
                .name("Device4")
                .brand("BrandC")
                .creationTime(LocalDateTime.now())
                .build();

        device = deviceRepository.save(device);

        Optional<Device> foundDevice = deviceRepository.findById(device.getId());

        assertThat(foundDevice).isPresent();
        assertThat(foundDevice.get().getName()).isEqualTo(device.getName());
    }
    /**
     * Tests saving a device.
     * Ensures the device is saved and can be retrieved correctly.
     */

    @Test
    void whenSaveDevice_thenDeviceIsSaved() {
        Device device = Device.builder()
                .name("Device5")
                .brand("BrandD")
                .creationTime(LocalDateTime.now())
                .build();

        device = deviceRepository.save(device);

        Optional<Device> foundDevice = deviceRepository.findById(device.getId());

        assertThat(foundDevice).isPresent();
        assertThat(foundDevice.get().getName()).isEqualTo("Device5");
    }
    /**
     * Tests deleting a device.
     * Ensures the device is deleted and cannot be retrieved.
     */

    @Test
    void whenDeleteDevice_thenDeviceIsDeleted() {
        Device device = Device.builder()
                .name("Device6")
                .brand("BrandE")
                .creationTime(LocalDateTime.now())
                .build();

        device = deviceRepository.save(device);
        Long id = device.getId();

        deviceRepository.delete(device);
        Optional<Device> foundDevice = deviceRepository.findById(id);

        assertThat(foundDevice).isNotPresent();
    }
}
