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

@DataJpaTest
@ActiveProfiles("test")
public class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

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

    @Test
    void whenFindByBrand_thenReturnDevices() {
        List<Device> devices = deviceRepository.findByBrand("BrandA");

        assertThat(devices).hasSize(2);
        assertThat(devices).extracting(Device::getName).containsExactlyInAnyOrder("Device1", "Device2");
    }

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
