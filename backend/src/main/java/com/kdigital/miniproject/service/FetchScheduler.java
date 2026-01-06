package com.kdigital.miniproject.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class FetchScheduler {
	private final FetchData fetch;
	
	// 실행시 동작
	//@Bean
	// 매일 1시에 동작
	//@Scheduled(cron = "0 0 1 * * *")
	// 매 짝수 시에 동작
	//@Scheduled(cron = "0 0 0/2 * * *")
	// 매일 10시 22시에 동작
	@Scheduled(cron = "0 0 10,22 * * *")
	public void fetchStart() {
		if(fetch == null)
			return ;
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		for(int i = 0; i < 7; ++i) {
			System.out.println(date.toString());
			fetch.setDate(date);
			fetch.startFetch();
			cal.add(Calendar.DATE, 1);
			date = cal.getTime();
		}
	}
}
