package com.tim.deliveryfee.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationObservation {

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "wmocode")
    private String wmoCode;

    @JacksonXmlProperty(localName = "airtemperature")
    private String airTemperature;

    @JacksonXmlProperty(localName = "windspeed")
    private String windSpeed;

    @JacksonXmlProperty(localName = "phenomenon")
    private String phenomenon;
}