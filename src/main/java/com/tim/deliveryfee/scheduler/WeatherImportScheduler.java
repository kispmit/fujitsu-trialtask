package com.tim.deliveryfee.scheduler;

import com.tim.deliveryfee.service.WeatherImportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherImportScheduler {

    private final WeatherImportService weatherImportService;

    public WeatherImportScheduler(WeatherImportService weatherImportService) {
        this.weatherImportService = weatherImportService;
    }

    @Scheduled(cron = "${weather.import.cron}")
    public void importWeather() {
        weatherImportService.importWeatherData();
    }
}