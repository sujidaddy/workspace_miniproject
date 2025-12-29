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
	// H2에서 초기화 될때 area_no를 지정할수 없기 때문에
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Builder.Default
	private int area_no = -1;
	private String area_name;
	private double center_lat;
	private double center_lng;
	private int radius;
	private String fillColor;
	private short level;
}
