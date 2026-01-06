package org.acme.inventory.grpc;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.inventory.model.*;
import org.acme.inventory.repository.CarRepository;

import java.util.Optional;

@GrpcService
public class GrpcInventoryService implements InventoryService {
    @Inject
    CarRepository carRepository;

    @Override
    public Uni<CarResponse> add(InsertCarRequest request) {
        Car car = new Car();
        car.manufacturer = request.getManufacturer();
        car.model = request.getModel();
        car.licensePlateNumber = request.getLicensePlateNumber();
        carRepository.persist(car);

        return Uni.createFrom().item(CarResponse.newBuilder()
                .setId(car.id)
                .setManufacturer(car.manufacturer)
                .setModel(car.model)
                .setLicensePlateNumber(car.licensePlateNumber)
                .build());
    }

    @Override
    @Blocking
    public Multi<CarResponse> addMulti(Multi<InsertCarRequest> requests) {
        return requests.map(request -> {
            Car car = new Car();
            car.manufacturer = request.getManufacturer();
            car.model = request.getModel();
            car.licensePlateNumber = request.getLicensePlateNumber();
            return car;
        }).onItem().invoke(car -> {
            Log.info("Car added to inventory " + car);
            carRepository.persist(car);
        }).map(car -> CarResponse.newBuilder()
                .setId(car.id)
                .setManufacturer(car.manufacturer)
                .setModel(car.model)
                .setLicensePlateNumber(car.licensePlateNumber)
                .build());
    }

    @Override
    @Blocking
    @Transactional
    public Uni<CarResponse> remove(RemoveCarRequest request) {
        Optional<Car> optionalCar = carRepository.findByLicensePlateNumberOptional(request.getLicensePlateNumber());
        if (optionalCar.isPresent()) {
            Car removedCar = optionalCar.get();
            carRepository.delete(removedCar);
            return Uni.createFrom().item(CarResponse.newBuilder()
                    .setId(removedCar.id)
                    .setModel(removedCar.model)
                    .setManufacturer(removedCar.manufacturer)
                    .setLicensePlateNumber(removedCar.licensePlateNumber)
                    .build());
        }
        return Uni.createFrom().nullItem();
    }
}
