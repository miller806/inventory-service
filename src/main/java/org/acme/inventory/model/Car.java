package org.acme.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.ToString;

/**
 * @author miller
 * @version 1.0.0
 * @since 2025/12/26
 */
@Entity
@ToString
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String licensePlateNumber;
    public String manufacturer;
    public String model;
}
