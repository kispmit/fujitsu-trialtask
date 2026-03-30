package com.tim.deliveryfee.service;

import com.tim.deliveryfee.domain.WeatherObservation;
import com.tim.deliveryfee.domain.enums.Vehicle;
import com.tim.deliveryfee.exception.VehicleForbiddenException;

import java.math.BigDecimal;

public class WeatherFeeCalculator {

    public BigDecimal calculate(WeatherObservation weather, Vehicle vehicle) {
        BigDecimal extraFee = BigDecimal.ZERO;

        // checking forbidden
        validateRestrictions(weather, vehicle);

        // checking temperature
        extraFee = extraFee.add(calculateTemperatureFee(weather, vehicle));

        // checking wind
        extraFee = extraFee.add(calculateWindFee(weather, vehicle));

        // checking condition / phenomenon
        extraFee = extraFee.add(calculateConditionFee(weather, vehicle));

        return extraFee;
    }

    // everything to lowercase
    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private void validateRestrictions(WeatherObservation weather, Vehicle vehicle) {
        BigDecimal windSpeed = weather.getWindSpeed();
        String condition = normalize(weather.getWeatherCondition());

        if (vehicle == Vehicle.BIKE && windSpeed.compareTo(BigDecimal.valueOf(20)) > 0) {
            throw new VehicleForbiddenException("Usage of selected vehicle type is forbidden");
        }

        if ((vehicle == Vehicle.BIKE || vehicle == Vehicle.SCOOTER)
                && (condition.contains("glaze")
                || condition.contains("hail")
                || condition.contains("thunder"))) {
            throw new VehicleForbiddenException("Usage of selected vehicle type is forbidden");
        }
    }

    private BigDecimal calculateTemperatureFee(WeatherObservation weather, Vehicle vehicle) {
        if (vehicle == Vehicle.CAR) {
            return BigDecimal.ZERO;
        }

        BigDecimal temperature = weather.getAirTemp();

        if (temperature.compareTo(BigDecimal.valueOf(-10)) < 0) {
            return BigDecimal.valueOf(1.0);
        }

        if (temperature.compareTo(BigDecimal.valueOf(-10)) >= 0
                && temperature.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.valueOf(0.5);
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateWindFee(WeatherObservation weather, Vehicle vehicle) {
        if (vehicle != Vehicle.BIKE) {
            return BigDecimal.ZERO;
        }

        BigDecimal windSpeed = weather.getWindSpeed();

        if (windSpeed.compareTo(BigDecimal.valueOf(10)) >= 0
                && windSpeed.compareTo(BigDecimal.valueOf(20)) <= 0) {
            return BigDecimal.valueOf(0.5);
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateConditionFee(WeatherObservation weather, Vehicle vehicle) {
        if (vehicle == Vehicle.CAR) {
            return BigDecimal.ZERO;
        }

        String condition = normalize(weather.getWeatherCondition());

        if (condition.contains("snow") || condition.contains("sleet")) {
            return BigDecimal.valueOf(1.0);
        }

        if (condition.contains("rain")) {
            return BigDecimal.valueOf(0.5);
        }

        return BigDecimal.ZERO;
    }
}