package com.kdigital.miniproject.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
	protected long fish_no;
	@JsonProperty("seafsTgfshNm")
	protected String name;
	protected double tdlvHrScr;
	protected String totalIndex;
	protected double lastScr;
	@ManyToOne
	@JoinColumn(name="weather_no")
	private Weather weather;
	@ManyToOne
	@JoinColumn(name="location_no")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Location location;
}
