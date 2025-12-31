package com.kdigital.miniproject.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.kdigital.miniproject.domain.FetchData;

@SuppressWarnings("unused")
@Configuration
public class FetchScheduler {
	
	@Autowired
	private FetchData fetch;
	
	// 실행시 동작
	//@Bean
	// 매일 1시에 동작
	@Scheduled(cron = "0 0 1 * * *")
	public String Init() {
		if(fetch == null)
			return "fetch not load";
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		for(int i = 0; i < 7; ++i) {
			System.out.println(date.toString());
			fetch.setDate(date);
			fetch.startFetch();
			cal.add(Calendar.DATE, 1);
			date = cal.getTime();
		}
		return "";
	}
}
