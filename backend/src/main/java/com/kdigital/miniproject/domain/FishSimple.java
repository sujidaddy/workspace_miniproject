package com.kdigital.miniproject.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class FishSimple extends Fish {
	private long weather_no;
	private long location_no;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Weather weather;
	public FishSimple(Fish fish) {
		this.fish_no = fish.fish_no;
		this.name = fish.name;
		this.tdvHrScr = fish.tdvHrScr;
		this.totalIndex = fish.totalIndex;
		this.lastScr = fish.lastScr;
		this.weather_no = fish.getWeather().getWeather_no();
		this.location_no = fish.getLocation().getLocation_no();
	}
}
