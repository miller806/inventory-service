package org.acme.inventory.grpc;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.acme.inventory.database.CarInventory;
import org.acme.inventory.model.*;

import java.util.Optional;

@GrpcService
public class GrpcInventoryService implements InventoryService {
    @Inject
    CarInventory carInventory;

    @Override
    public Uni<CarResponse> add(InsertCarRequest request) {
        Car car = new Car();
        car.id = CarInventory.ids.incrementAndGet();
        car.manufacturer = request.getManufacturer();
        car.model = request.getModel();
        car.licensePlateNumber = request.getLicensePlateNumber();
        carInventory.getCars().add(car);

        return Uni.createFrom().item(CarResponse.newBuilder()
                .setId(car.id)
                .setManufacturer(car.manufacturer)
                .setModel(car.model)
                .setLicensePlateNumber(car.licensePlateNumber)
                .build());
    }

    @Override
    public Multi<CarResponse> addMulti(Multi<InsertCarRequest> requests) {
        return requests.map(request -> {
            Car car = new Car();
            car.id = CarInventory.ids.incrementAndGet();
            car.manufacturer = request.getManufacturer();
            car.model = request.getModel();
            car.licensePlateNumber = request.getLicensePlateNumber();
            return car;
        }).onItem().invoke(car -> {
            Log.info("Car added to inventory " + car);
            carInventory.getCars().add(car);
        }).map(car -> CarResponse.newBuilder()
                .setId(car.id)
                .setManufacturer(car.manufacturer)
                .setModel(car.model)
                .setLicensePlateNumber(car.licensePlateNumber)
                .build());
    }

    @Override
    public Uni<CarResponse> remove(RemoveCarRequest request) {
        Optional<Car> optionalCar = carInventory.getCars().stream().filter(
                car -> request.getLicensePlateNumber().equals(car.licensePlateNumber)
        ).findFirst();
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            carInventory.getCars().remove(car);
            return Uni.createFrom().item(CarResponse.newBuilder()
                    .setId(car.id)
                    .setModel(car.model)
                    .setManufacturer(car.manufacturer)
                    .setLicensePlateNumber(car.licensePlateNumber)
                    .build());
        }
        return Uni.createFrom().nullItem();
    }
}
