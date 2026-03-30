package com.tim.deliveryfee.service;

import com.tim.deliveryfee.domain.WeatherObservation;
import com.tim.deliveryfee.domain.enums.Vehicle;
import com.tim.deliveryfee.exception.VehicleForbiddenException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeatherFeeCalculatorTest {

    private final WeatherFeeCalculator calculator = new WeatherFeeCalculator();

    @Test
    void shouldAddTemperatureAndSnowFeeForBike() {
        WeatherObservation weather = createWeather("-2.1", "4.7", "Light snow shower");

        BigDecimal fee = calculator.calculate(weather, Vehicle.BIKE);

        assertEquals(BigDecimal.valueOf(1.5), fee);
    }

    @Test
    void shouldAddOneEuroWhenTemperatureIsLessThanMinusTen() {
        WeatherObservation weather = createWeather("-12.0", "3.0", "Clear");

        BigDecimal fee = calculator.calculate(weather, Vehicle.SCOOTER);

        assertEquals(BigDecimal.valueOf(1.0), fee);
    }

    @Test
    void shouldAddHalfEuroWhenTemperatureIsBetweenMinusTenAndZero() {
        WeatherObservation weather = createWeather("-5.0", "3.0", "Clear");

        BigDecimal fee = calculator.calculate(weather, Vehicle.SCOOTER);

        assertEquals(BigDecimal.valueOf(0.5), fee);
    }

    @Test
    void shouldAddHalfEuroForBikeWhenWindIsBetweenTenAndTwenty() {
        WeatherObservation weather = createWeather("5.0", "15.0", "Clear");

        BigDecimal fee = calculator.calculate(weather, Vehicle.BIKE);

        assertEquals(BigDecimal.valueOf(0.5), fee);
    }

    @Test
    void shouldThrowExceptionWhenBikeWindSpeedIsAboveTwenty() {
        WeatherObservation weather = createWeather("5.0", "21.0", "Clear");

        assertThrows(VehicleForbiddenException.class,
                () -> calculator.calculate(weather, Vehicle.BIKE));
    }

    @Test
    void shouldThrowExceptionWhenScooterConditionIsThunder() {
        WeatherObservation weather = createWeather("5.0", "3.0", "Thunder");

        assertThrows(VehicleForbiddenException.class,
                () -> calculator.calculate(weather, Vehicle.SCOOTER));
    }

    @Test
    void shouldAddHalfEuroWhenConditionIsRain() {
        WeatherObservation weather = createWeather("5.0", "3.0", "Light rain");

        BigDecimal fee = calculator.calculate(weather, Vehicle.SCOOTER);

        assertEquals(BigDecimal.valueOf(0.5), fee);
    }

    @Test
    void shouldReturnZeroForCarRegardlessOfWeatherUnlessNoRestrictionApplies() {
        WeatherObservation weather = createWeather("-15.0", "15.0", "Snow");

        BigDecimal fee = calculator.calculate(weather, Vehicle.CAR);

        assertEquals(BigDecimal.ZERO, fee);
    }

    private WeatherObservation createWeather(String airTemp, String windSpeed, String condition) {
        WeatherObservation weather = new WeatherObservation();
        weather.setAirTemp(new BigDecimal(airTemp));
        weather.setWindSpeed(new BigDecimal(windSpeed));
        weather.setWeatherCondition(condition);
        return weather;
    }
}