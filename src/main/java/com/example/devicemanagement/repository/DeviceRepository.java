package com.example.devicemanagement.repository;

import com.example.devicemanagement.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The DeviceRepository interface extends JpaRepository to provide CRUD operations for the Device entity.
 * It also includes a custom query method to find devices by their brand.
 */

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    /**
     * Finds a list of devices by their brand.
     *
     * @param brand the brand of the devices to find
     * @return a list of devices with the specified brand
     */

    List<Device> findByBrand(String brand);
}
