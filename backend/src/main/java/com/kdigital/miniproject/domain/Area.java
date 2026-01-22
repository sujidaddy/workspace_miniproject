package com.kdigital.miniproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Area {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Builder.Default
	private int area_no;
	private String area_name;
	private double center_lat;
	private double center_lng;
	private int radius;
	private String fillColor;
	private short level;
}
