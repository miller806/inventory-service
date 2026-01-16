package org.acme.inventory.health;

import io.smallrye.health.api.Wellness;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.inventory.repository.CarRepository;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 * @author miller
 * @version 1.0.0
 * @since 2026/1/16
 */
@Wellness
public class CarCountCheck implements HealthCheck {
    @Inject
    CarRepository carRepository;

    @Override
    @Transactional
    public HealthCheckResponse call() {
        long carCount = carRepository.findAll().count();
        boolean hasCar = carCount > 0;
        return HealthCheckResponse.builder().name("car-count-check")
                .status(hasCar)
                .withData("cars-count", carCount).build();
    }
}
