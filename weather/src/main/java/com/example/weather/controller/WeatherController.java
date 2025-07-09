package com.example.weather.controller;

import com.example.weather.dto.WeatherResponse;
import com.example.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService = new WeatherService(null, null, null);

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam String pincode,
            @RequestParam("for_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate forDate) {
    	
    	System.out.println("Fetching weather for Pincode: " + pincode + ", Date: " + forDate);
        return ResponseEntity.ok(weatherService.getWeatherByPincodeAndDate(pincode, forDate));
    }
}
