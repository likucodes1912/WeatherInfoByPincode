package com.example.weather.service;

import com.example.weather.dto.WeatherResponse;
import com.example.weather.entity.Location;
import com.example.weather.entity.Weather;
import com.example.weather.repository.LocationRepository;
import com.example.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepo;
    private final LocationRepository locationRepo;
    private final RestTemplate restTemplate;

    @Value("${openweather.api.key}")
    private String weatherApiKey;

    // ✅ Manual constructor injection
    public WeatherService(WeatherRepository weatherRepo,
                          LocationRepository locationRepo,
                          RestTemplate restTemplate) {
        this.weatherRepo = weatherRepo;
        this.locationRepo = locationRepo;
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeatherByPincodeAndDate(String pincode, LocalDate forDate) {
        System.out.println("Fetching weather for Pincode: " + pincode + ", Date: " + forDate);

        // Step 1: Check DB cache
        Optional<Weather> cached = weatherRepo.findByPincodeAndDate(pincode, forDate);
        if (cached.isPresent()) {
            return WeatherResponse.from(cached.get());
        }

        // Step 2: Get location (lat/lon)
        Location location = locationRepo.findByPincode(pincode).orElseGet(() -> {
            String geoUrl = "http://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",IN&appid=" + weatherApiKey;
            Map<String, Object> body = restTemplate.getForObject(geoUrl, Map.class);

            if (body == null || body.get("lat") == null || body.get("lon") == null) {
                throw new RuntimeException("Geo API failed or returned incomplete data");
            }

            double lat = ((Number) body.get("lat")).doubleValue();
            double lon = ((Number) body.get("lon")).doubleValue();

            System.out.println("Resolved coordinates: lat=" + lat + ", lon=" + lon);

            Location loc = new Location(pincode, lat, lon);
            return locationRepo.save(loc);
        });

        // Step 3: Fetch weather data
        String weatherUrl = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
            location.getLat(), location.getLon(), weatherApiKey
        );

        Map<String, Object> weatherData = restTemplate.getForObject(weatherUrl, Map.class);

        if (weatherData == null || !weatherData.containsKey("weather") || !weatherData.containsKey("main")) {
            throw new RuntimeException("Weather API failed or returned incomplete data");
        }

        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherData.get("weather");
        Map<String, Object> mainMap = (Map<String, Object>) weatherData.get("main");

        if (weatherList == null || weatherList.isEmpty() || mainMap == null) {
            throw new RuntimeException("Invalid structure in weather API response");
        }

        String description = weatherList.get(0).get("description").toString();
        double temperature = ((Number) mainMap.get("temp")).doubleValue();

        System.out.println("Weather description: " + description + ", Temp: " + temperature);

        // Step 4: Save to DB
        Weather weather = new Weather(pincode, forDate, description, temperature);
        weatherRepo.save(weather);

        // Step 5: Return response
        return WeatherResponse.from(weather);
    }
}
