package com.kdigital.miniproject.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSession {
	private String username;
	private Role role;
	private String provider;
}