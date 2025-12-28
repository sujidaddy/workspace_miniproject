package com.kdigital.miniproject.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ResponseDTO {
	private boolean success;
	private List<Object> data;
	private String error;
	
	public void addData(Object obj) {
		if(data == null)
			data = new ArrayList<>();
		data.add(obj);
	}
}
