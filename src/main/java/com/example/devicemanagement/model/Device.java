package com.example.devicemanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity class representing a device in the database.
 */
@Entity // Marks this class as a JPA entity.
@Table(name = "devices") // Maps the entity to the "devices" table.
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods.
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor.
@AllArgsConstructor // Lombok annotation to generate an all-argument constructor.
@Builder // Lombok annotation to implement the Builder pattern.
public class Device {

    @Id // Marks this field as the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies the primary key generation strategy.
    private Long id;

    @Column(nullable = false) // Maps this field to a column that cannot be null.
    private String name;

    @Column(nullable = false)
    private String brand;

    @CreationTimestamp // Automatically sets this field to the current timestamp when the entity is created.
    private LocalDateTime creationTime;
}
