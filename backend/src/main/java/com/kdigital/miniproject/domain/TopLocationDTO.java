package com.kdigital.miniproject.domain;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class TopLocationDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long data_no;
	@Builder.Default
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@CreatedDate
	@Column(updatable = false)
	LocalDate createDate = LocalDate.now();
	String seafsPstnNm;
	double lat;
	double lot;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate predcYmd;
	String predcNonSeCd;
	String seafsTgfshNm;
	double tdlvHrScr;
	double minWvhgt;
	double maxWvhgt;
	double minWtem;
	double maxWtem;
	double minArtmp;
	double maxArtmp;
	double minCrsp;
	double maxCrsp;
	double minWspd;
	double maxWspd;
	String totalIndex;
	double lastScr;
	
	public TopLocationDTO(Fish f) {
		this.createDate = LocalDate.now();
		this.seafsPstnNm = f.getLocation().name;
		this.lat = f.getLocation().lat;
		this.lot = f.getLocation().lot;
		this.predcYmd = f.getWeather().predcYmd;
		this.predcNonSeCd = f.getWeather().predcNoonSeCd;
		this.seafsTgfshNm = f.name;
		this.tdlvHrScr = f.tdlvHrScr;
		this.minWvhgt = f.getWeather().minWvhgt;
		this.maxWvhgt = f.getWeather().maxWvhgt;
		this.minWtem = f.getWeather().minWtem;
		this.maxWtem = f.getWeather().maxWtem;
		this.minArtmp = f.getWeather().minArtmp;
		this.maxArtmp = f.getWeather().maxArtmp;
		this.minCrsp = f.getWeather().minCrsp;
		this.maxCrsp = f.getWeather().maxCrsp;
		this.minWspd = f.getWeather().minWspd;
		this.maxWspd = f.getWeather().maxWspd;
		this.totalIndex = f.totalIndex;
		this.lastScr = f.lastScr;
	}
}
