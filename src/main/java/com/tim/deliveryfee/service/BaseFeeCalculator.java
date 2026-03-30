package com.tim.deliveryfee.service;

import com.tim.deliveryfee.domain.enums.City;
import com.tim.deliveryfee.domain.enums.Vehicle;

import java.math.BigDecimal;
import java.util.Map;

public class BaseFeeCalculator {

    private static final Map<City, Map<Vehicle, BigDecimal>> BASE_FEES = Map.of(
            City.TALLINN, Map.of(
                    Vehicle.CAR, BigDecimal.valueOf(4.0),
                    Vehicle.SCOOTER, BigDecimal.valueOf(3.5),
                    Vehicle.BIKE, BigDecimal.valueOf(3.0)
            ),
            City.TARTU, Map.of(
                    Vehicle.CAR, BigDecimal.valueOf(3.5),
                    Vehicle.SCOOTER, BigDecimal.valueOf(3),
                    Vehicle.BIKE, BigDecimal.valueOf(2.5)
            ),
            City.PARNU, Map.of(
                    Vehicle.CAR, BigDecimal.valueOf(3.0),
                    Vehicle.SCOOTER, BigDecimal.valueOf(2.5),
                    Vehicle.BIKE, BigDecimal.valueOf(2.0)
            )
    );

    public BigDecimal calculateBaseFee(City city, Vehicle vehicle) {
        Map<Vehicle, BigDecimal> cityFee = BASE_FEES.get(city);

        if (cityFee == null || !cityFee.containsKey(vehicle)) {
            throw new IllegalArgumentException("City " + city + " has no fee for " + vehicle);
        }
        return cityFee.get(vehicle);
    }
}
