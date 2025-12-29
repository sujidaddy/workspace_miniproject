package com.kdigital.miniproject.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.kdigital.miniproject.controller.FishController;
import com.kdigital.miniproject.service.FishService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherSimple extends Weather {
	private long location_no;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Location location;
	List<FishSimple> fish_list;
	public WeatherSimple(Weather wea, FishService fishservice) {
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
		this.fish_list = FishController.toSimple(fishservice.getFishList(Weather.builder().weather_no(this.weather_no).build()));
	}
}
