package com.kdigital.miniproject.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FishSimple extends Fish {
	protected Date predcYmd;
	protected String predcNoonSeCd;
	protected double minWvhgt;
	protected double maxWvhgt;
	protected double minWtem;
	protected double maxWtem;
	protected double minArtmp;
	protected double maxArtmp;
	protected double minCrsp;
	protected double maxCrsp;
	protected double minWspd;
	protected double maxWspd;
	
	@JsonProperty("seafsPstnNm")
	protected String area_name;
	protected double lat;
	protected double lot;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Weather weather;
	public FishSimple(Fish fish) {
		this.fish_no = fish.fish_no;
		this.name = fish.name;
		this.tdvHrScr = fish.tdvHrScr;
		this.totalIndex = fish.totalIndex;
		this.lastScr = fish.lastScr;
		Weather wea = fish.getWeather();
		Location loc = fish.getLocation();
		
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
		
		this.area_name = loc.name;
		this.lat = loc.lat;
		this.lot = loc.lot;
	}
}
