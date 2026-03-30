package com.tim.deliveryfee.service;

import com.tim.deliveryfee.config.StationMapping;
import com.tim.deliveryfee.domain.WeatherObservation;
import com.tim.deliveryfee.domain.enums.City;
import com.tim.deliveryfee.domain.enums.Vehicle;
import com.tim.deliveryfee.exception.WeatherDataNotFoundException;
import com.tim.deliveryfee.repo.WeatherObservationRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DeliveryFeeService {

    private final WeatherObservationRepo weatherObservationRepo;
    private final BaseFeeCalculator baseFeeCalculator;
    private final WeatherFeeCalculator weatherFeeCalculator;

    public DeliveryFeeService(WeatherObservationRepo weatherObservationRepo) {
        this.weatherObservationRepo = weatherObservationRepo;
        this.baseFeeCalculator = new BaseFeeCalculator();
        this.weatherFeeCalculator = new WeatherFeeCalculator();
    }
    public BigDecimal calculateFee(City city, Vehicle vehicle) {
        String stationName = StationMapping.CITY_TO_STATION.get(city);

        WeatherObservation latestWeather = weatherObservationRepo
                .findTopByStationNameOrderByObservationTimeDesc(stationName)
                .orElseThrow(() -> new WeatherDataNotFoundException("No weather data found for station: " + stationName));

        BigDecimal baseFee = baseFeeCalculator.calculateBaseFee(city, vehicle);
        BigDecimal weatherFee = weatherFeeCalculator.calculate(latestWeather, vehicle);

        return baseFee.add(weatherFee);
    }
}
