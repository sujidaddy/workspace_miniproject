package com.kdigital.miniproject.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class Weather {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long weather_no;
	@Temporal(TemporalType.DATE)
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
	@ManyToOne
	@JoinColumn(name="location_no")
	private Location location;
}
