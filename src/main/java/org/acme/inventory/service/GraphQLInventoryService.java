package org.acme.inventory.service;

import io.quarkus.runtime.ApplicationConfig;
import jakarta.inject.Inject;
import org.acme.inventory.database.CarInventory;
import org.acme.inventory.model.Car;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author miller
 * @version 1.0.0
 * @since 2025/12/26
 */
@GraphQLApi
public class GraphQLInventoryService {
    @Inject
    CarInventory carInventory;
    @Inject
    ApplicationConfig applicationConfig;

    @Query
        public List<Car> cars() {
        return carInventory.getCars();
    }

    @Mutation
    public Car register(Car car) {
        car.id = CarInventory.ids.incrementAndGet();
        carInventory.getCars().add(car);
        return car;
    }

    @Mutation
    public boolean remove(String licensePlateNumber) {
        List<Car> cars = carInventory.getCars();
        Optional<Car> toBeRemoved = cars.stream().filter(car -> car.licensePlateNumber.equals(licensePlateNumber))
                .findAny();
        if (toBeRemoved.isPresent()) {
            return cars.remove(toBeRemoved.get());
        } else {
            return false;
        }
    }
}
