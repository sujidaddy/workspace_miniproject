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
public class LocationSimple extends Location {
	private long area_no;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Area area;
	public LocationSimple(Location loc) {
		this.location_no = loc.location_no;
		this.name = loc.name;
		this.lat = loc.lat;
		this.lot = loc.lot;
		this.area_no = loc.getArea().getArea_no();
	}
}
