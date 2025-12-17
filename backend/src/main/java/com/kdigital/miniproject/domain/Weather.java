package com.kdigital.miniproject.domain;

import java.util.Date;

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
	private long weather_no;
	@Temporal(TemporalType.DATE)
	private Date predcYmd;
	private String predcNoonSeCd;
	private double minWvhgt;
	private double maxWvhgt;
	private double minWtem;
	private double maxWtem;
	private double minArtmp;
	private double maxArtmp;
	private double minCrsp;
	private double maxCrsp;
	private double minWspd;
	private double maxWspd;
	@ManyToOne
	@JoinColumn(name="location_no")
	private Location location;
}
