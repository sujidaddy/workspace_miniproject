package com.kdigital.miniproject.domain;

import java.time.LocalDate;

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
public class FetchLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long logNo;
	private String fetchUrl;
	@Column(columnDefinition = "TEXT")
	private String errorMsg;
	private int locationCount;
	private int weatherCount;
	private int weatherUpdate;
	private int fishCount;
	private int fishUpdate;
	@Builder.Default
	@Temporal(TemporalType.DATE)
	//@Column(columnDefinition = "date default (curdate())")
	// Supabase 전환 curdate() => CURRENT_DATE
	@Column(columnDefinition = "date default CURRENT_DATE")
	private LocalDate createDate = LocalDate.now();

}
