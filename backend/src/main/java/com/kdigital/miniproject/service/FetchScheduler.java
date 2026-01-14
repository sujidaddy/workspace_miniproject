package com.kdigital.miniproject.service;

import java.time.LocalDate;
import java.util.Calendar;

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
		boolean fetchAll = false;
		if(fetchAll)
		{
			LocalDate curDate = LocalDate.now();
			for(int i = 0; i < 7; ++i) {
				System.out.println(curDate.toString());
				fetch.setDate(curDate);
				fetch.startFetch();
				curDate.plusDays(1);
			}
		}
		fetch.fetchTop3(LocalDate.now());
	}
}
