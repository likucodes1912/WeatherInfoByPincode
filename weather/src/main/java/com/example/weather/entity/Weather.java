package com.example.weather.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pincode;
    private LocalDate date;
    private String description;
    private double temperature;

    public Weather(String pincode, LocalDate date, String description, double temperature) {
        this.pincode = pincode;
        this.date = date;
        this.description = description;
        this.temperature = temperature;
    }

	public Object getPincode() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getTemperature() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
}