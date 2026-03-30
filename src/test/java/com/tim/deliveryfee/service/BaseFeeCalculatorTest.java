package com.tim.deliveryfee.service;

import com.tim.deliveryfee.domain.enums.City;
import com.tim.deliveryfee.domain.enums.Vehicle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseFeeCalculatorTest {

    private final BaseFeeCalculator calculator = new BaseFeeCalculator();

    @Test
    void shouldReturnTallinnCarBaseFee() {
        BigDecimal fee = calculator.calculateBaseFee(City.TALLINN, Vehicle.CAR);
        assertEquals(BigDecimal.valueOf(4.0), fee);
    }

    @Test
    void shouldReturnTartuBikeBaseFee() {
        BigDecimal fee = calculator.calculateBaseFee(City.TARTU, Vehicle.BIKE);
        assertEquals(BigDecimal.valueOf(2.5), fee);
    }

    @Test
    void shouldReturnParnuScooterBaseFee() {
        BigDecimal fee = calculator.calculateBaseFee(City.PARNU, Vehicle.SCOOTER);
        assertEquals(BigDecimal.valueOf(2.5), fee);
    }
}