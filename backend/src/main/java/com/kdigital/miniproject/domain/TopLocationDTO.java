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
import lombok.Builder.Default;
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
//	@Builder.Default
//	private long data_no = -1;
	private long data_no;
	@Builder.Default
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@CreatedDate
	@Column(updatable = false)
	private LocalDate createDate = LocalDate.now();
	private String seafsPstnNm;
	private double lat;
	private double lot;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate predcYmd;
	private String predcNoonSeCd;
	private String seafsTgfshNm;
	private double tdlvHrScr;
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
	private String totalIndex;
	private double lastScr;
	private long location_no;
	
	public TopLocationDTO(Fish f) {
		this.createDate = LocalDate.now();
		this.location_no = f.getLocation().location_no;
		this.seafsPstnNm = f.getLocation().name;
		this.lat = f.getLocation().lat;
		this.lot = f.getLocation().lot;
		this.predcYmd = f.getWeather().predcYmd;
		this.predcNoonSeCd = f.getWeather().predcNoonSeCd;
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
