package com.example.weather.dto;

import com.example.weather.entity.Weather;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private String pincode;
    private LocalDate date;
    private String description;
    private double temperature;

    public WeatherResponse(Object pincode2, Object date2, Object description2, Object temperature2) {
		// TODO Auto-generated constructor stub
	}

	public static WeatherResponse from(Weather weather) {
        return new WeatherResponse(
                weather.getPincode(),
                weather.getDate(),
                weather.getDescription(),
                weather.getTemperature()
        );
    }
}
