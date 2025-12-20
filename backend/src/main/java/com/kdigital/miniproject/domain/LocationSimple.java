package com.kdigital.miniproject.domain;

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
public class LocationSimple extends Location {
	private long area_no;
	public LocationSimple(Location loc) {
		this.location_no = loc.getLocation_no();
		this.name = loc.getName();
		this.lat = loc.getLat();
		this.lot = loc.getLot();
		this.area_no = loc.getArea().getArea_no();
	}
}
