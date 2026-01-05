package com.kdigital.miniproject.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModifyMemberDTO{
	private String currentPassword;
	private String newPassword;
	private String newUsername;
}