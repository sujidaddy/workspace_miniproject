package com.kdigital.miniproject.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
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
public class LocationFavorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long favorite_no;
	@ManyToOne
	@JoinColumn(name="user_no")
	@JsonProperty(access = Access.WRITE_ONLY)
	protected Member member;
	@ManyToOne
	@JoinColumn(name="location_no")
	@JsonProperty(access = Access.WRITE_ONLY)
	protected Location location;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	protected LocalDateTime createTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Builder.Default
	@JsonProperty(access = Access.WRITE_ONLY)
	protected LocalDateTime deleteTime = null;
}
