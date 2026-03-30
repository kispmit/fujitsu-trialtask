package com.tim.deliveryfee.repo;

import com.tim.deliveryfee.domain.WeatherObservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherObservationRepo extends JpaRepository<WeatherObservation, Long> {

    Optional<WeatherObservation> findTopByStationNameOrderByObservationTimeDesc(String stationName);
}
