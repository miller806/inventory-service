package org.acme.inventory.database;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.inventory.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author miller
 * @version 1.0.0
 * @since 2025/12/26
 */
@ApplicationScoped
public class CarInventory {
    private List<Car> cars;
    public static final AtomicLong ids = new AtomicLong(0);

    @PostConstruct
    void init() {
        cars = new CopyOnWriteArrayList<>();
        initData();
    }

    public List<Car> getCars() {
        return cars;
    }

    private void initData() {
        Car mazda = new Car();
        mazda.id = ids.incrementAndGet();
        mazda.manufacturer = "Mazda";
        mazda.model = "6";
        mazda.licensePlateNumber = "ABC123";
        cars.add(mazda);
        Car ford = new Car();
        ford.id = ids.incrementAndGet();
        ford.manufacturer = "Ford";
        ford.model = "Mustang";
        ford.licensePlateNumber = "XYZ987";
        cars.add(ford);
    }
}
