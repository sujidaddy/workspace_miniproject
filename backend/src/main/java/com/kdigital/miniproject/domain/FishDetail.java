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
public class FishDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int data_no;
	protected String name;
	protected String detail;
	protected String url;
}
