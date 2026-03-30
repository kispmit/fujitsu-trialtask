package com.tim.deliveryfee.config;

import com.tim.deliveryfee.domain.enums.City;

import java.util.Map;

public final class StationMapping {

    public static final Map<City, String> CITY_TO_STATION = Map.of(
            City.TALLINN, "Tallinn-Harku",
            City.TARTU, "Tartu-Tõravere",
            City.PARNU, "Pärnu"
    );
    private StationMapping() {}
}
