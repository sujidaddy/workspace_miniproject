package com.kdigital.miniproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Fish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fish_no;
	private String name;
	private double tdvHrScr;
	private String totalIndex;
	private double lastScr;
	@ManyToOne
	@JoinColumn(name="weather_no")
	private Weather weather;
	@ManyToOne
	@JoinColumn(name="location_no")
	private Location location;
}
