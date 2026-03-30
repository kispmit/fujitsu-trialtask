package com.tim.deliveryfee.service;

import com.tim.deliveryfee.domain.WeatherObservation;
import com.tim.deliveryfee.domain.enums.City;
import com.tim.deliveryfee.domain.enums.Vehicle;
import com.tim.deliveryfee.exception.WeatherDataNotFoundException;
import com.tim.deliveryfee.repo.WeatherObservationRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryFeeServiceTest {

    @Mock
    private WeatherObservationRepo weatherObservationRepo;

    @InjectMocks
    private DeliveryFeeService deliveryFeeService;

    @Test
    void shouldCalculateTartuBikeFeeUsingLatestWeather() {
        WeatherObservation weather = new WeatherObservation();
        weather.setStationName("Tartu-Tõravere");
        weather.setWmoCode("26242");
        weather.setAirTemp(new BigDecimal("-2.1"));
        weather.setWindSpeed(new BigDecimal("4.7"));
        weather.setWeatherCondition("Light snow shower");
        weather.setObservationTime(LocalDateTime.now());

        when(weatherObservationRepo.findTopByStationNameOrderByObservationTimeDesc("Tartu-Tõravere"))
                .thenReturn(Optional.of(weather));

        BigDecimal fee = deliveryFeeService.calculateFee(City.TARTU, Vehicle.BIKE);

        assertEquals(BigDecimal.valueOf(4.0), fee);
    }

    @Test
    void shouldThrowExceptionWhenNoWeatherDataExists() {
        when(weatherObservationRepo.findTopByStationNameOrderByObservationTimeDesc("Tartu-Tõravere"))
                .thenReturn(Optional.empty());

        assertThrows(WeatherDataNotFoundException.class,
                () -> deliveryFeeService.calculateFee(City.TARTU, Vehicle.BIKE));
    }
}