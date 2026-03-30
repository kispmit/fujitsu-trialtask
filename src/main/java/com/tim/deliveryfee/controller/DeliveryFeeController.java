package com.tim.deliveryfee.controller;

import com.tim.deliveryfee.domain.enums.City;
import com.tim.deliveryfee.domain.enums.Vehicle;
import com.tim.deliveryfee.service.DeliveryFeeService;
import com.tim.deliveryfee.service.WeatherImportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;
    private final WeatherImportService weatherImportService;

    public DeliveryFeeController(DeliveryFeeService deliveryFeeService,
                                 WeatherImportService weatherImportService) {
        this.deliveryFeeService = deliveryFeeService;
        this.weatherImportService = weatherImportService;
    }

    @GetMapping("/api/delivery-fee")
    public Map<String, BigDecimal> getDeliveryFee(
            @RequestParam City city,
            @RequestParam Vehicle vehicle
    ) {
        BigDecimal fee = deliveryFeeService.calculateFee(city, vehicle);
        return Map.of("fee", fee);
    }

    @GetMapping("/api/import-weather")
    public String importWeather() {
        weatherImportService.importWeatherData();
        return "Weather imported";
    }
}