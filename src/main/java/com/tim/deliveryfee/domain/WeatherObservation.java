package com.tim.deliveryfee.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_observations")
@Getter
@Setter
@NoArgsConstructor
public class WeatherObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    private String wmoCode;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal airTemp;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal windSpeed;

    private String weatherCondition;

    @Column(nullable = false)
    private LocalDateTime observationTime;
}
