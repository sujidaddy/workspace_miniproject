package com.kdigital.miniproject.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.kdigital.miniproject.controller.FishController;
import com.kdigital.miniproject.persistence.FishRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherChart{
	private Date predcYmd;
	private String type;
	private double minValue;
	private double maxValue;
	
	public WeatherChart(Weather wea, String query)
	{
		type = query;
		this.predcYmd = wea.predcYmd;
		switch(query) {
		case "파고":
			this.minValue = wea.minWvhgt;
			this.maxValue = wea.maxWvhgt;
			break;
		case "풍속":
			this.minValue = wea.minWspd;
			this.maxValue = wea.maxWspd;
			break;
		case "수온":
			this.minValue = wea.minWtem;
			this.maxValue = wea.maxWtem;
			break;
		case "유속":
			this.minValue = wea.minCrsp;
			this.maxValue = wea.maxCrsp;
			break;
	}
	}
}
