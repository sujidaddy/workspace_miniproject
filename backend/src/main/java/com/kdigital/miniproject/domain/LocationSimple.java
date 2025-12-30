package com.kdigital.miniproject.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationSimple extends Location{
	private int area_no;
	private String area_name;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Area area;
	public LocationSimple(Location loc) {
		this.location_no = loc.location_no;
		this.name = loc.name;
		this.lat = loc.lat;
		this.lot = loc.lot;
		this.area_no = loc.getArea().getArea_no();
		this.area_name = loc.getArea().getArea_name();
	}
}
