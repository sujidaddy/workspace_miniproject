package com.kdigital.miniproject.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginSession {
	private String username;
	private Role role;
	private String provider;
}