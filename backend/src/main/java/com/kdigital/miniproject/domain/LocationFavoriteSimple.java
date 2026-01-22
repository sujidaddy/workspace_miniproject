package com.kdigital.miniproject.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationFavoriteSimple extends LocationFavorite {
	private long location_no;
	public LocationFavoriteSimple(LocationFavorite favorite) {
		this.favorite_no = favorite.favorite_no;
		this.location_no = favorite.location.location_no;
		
	}
}
