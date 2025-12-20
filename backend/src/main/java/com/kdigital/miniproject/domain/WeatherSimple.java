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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSimple extends Weather {
	private long location_no;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Location location;
	public WeatherSimple(Weather wea) {
		this.weather_no = wea.weather_no;
		this.predcYmd = wea.predcYmd;
		this.predcNoonSeCd = wea.predcNoonSeCd;
		this.minWvhgt = wea.minWvhgt;
		this.maxWvhgt = wea.maxWvhgt;
		this.minWtem = wea.minWtem;
		this.maxWtem = wea.maxWtem;
		this.minArtmp = wea.minArtmp;
		this.maxArtmp = wea.maxArtmp;
		this.minCrsp = wea.minCrsp;
		this.maxCrsp = wea.maxCrsp;
		this.minWspd = wea.minWspd;
		this.maxWspd = wea.maxWspd;
		this.location_no = wea.getLocation().getLocation_no();
	}
}
