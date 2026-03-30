package com.tim.deliveryfee.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.tim.deliveryfee.domain.WeatherObservation;
import com.tim.deliveryfee.dto.ObservationsResponse;
import com.tim.deliveryfee.dto.StationObservation;
import com.tim.deliveryfee.repo.WeatherObservationRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
public class WeatherImportService {

    private static final String WEATHER_URL =
            "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    private static final Set<String> TARGET_STATIONS = Set.of(
            "Tallinn-Harku",
            "Tartu-Tõravere",
            "Pärnu"
    );

    //importing on startup
    @PostConstruct
    public void init() {
        importWeatherData();
    }

    private final WeatherObservationRepo weatherObservationRepo;
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;

    public WeatherImportService(WeatherObservationRepo weatherObservationRepo) {
        this.weatherObservationRepo = weatherObservationRepo;
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    public void importWeatherData() {
        try {
            String xmlResponse = restTemplate.getForObject(WEATHER_URL, String.class);

            if (xmlResponse == null || xmlResponse.isBlank()) {
                throw new RuntimeException("Weather API returned empty");
            }

            ObservationsResponse observationsResponse =
                    xmlMapper.readValue(xmlResponse, ObservationsResponse.class);
            /*
            observationsResponse.getStations()
                    .forEach(s -> System.out.println("Condition: " + s.getPhenomenon()));
            */

            if (observationsResponse.getStations() == null || observationsResponse.getStations().isEmpty()) {
                throw new RuntimeException("No station data found in weather API response");
            }

            List<WeatherObservation> weatherObservations = observationsResponse.getStations().stream()
                    .filter(station -> TARGET_STATIONS.contains(station.getName()))
                    .map(this::toWeatherObservation)
                    .toList();
            /*
            System.out.println("Stations parsed: " + observationsResponse.getStations().size());
            observationsResponse.getStations()
                    .forEach(station -> System.out.println("Station: " + station.getName()));
            System.out.println("Filtered rows: " + weatherObservations.size());
            */
            weatherObservationRepo.saveAll(weatherObservations);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import weather data", e);
        }
    }

    private WeatherObservation toWeatherObservation(StationObservation station) {
        WeatherObservation weatherObservation = new WeatherObservation();
        weatherObservation.setStationName(station.getName());
        weatherObservation.setWmoCode(station.getWmoCode());
        weatherObservation.setAirTemp(parseBigDecimal(station.getAirTemperature()));
        weatherObservation.setWindSpeed(parseBigDecimal(station.getWindSpeed()));
        weatherObservation.setWeatherCondition(station.getPhenomenon());
        weatherObservation.setObservationTime(LocalDateTime.now());
        return weatherObservation;
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.trim());
    }
}