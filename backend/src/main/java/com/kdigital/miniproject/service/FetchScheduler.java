package com.kdigital.miniproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.kdigital.miniproject.domain.FetchData;

@Configuration
public class FetchScheduler {
	
	@Autowired
	private FetchData fetch;
	
	@Bean
	@Scheduled(cron = "0 0 1,13 * * *")
	public String Init() {
		if(fetch == null)
			return "fetch not load";
		fetch.startFetch();
		return "";
	}
	/*
	//@Scheduled(cron = "45 21 10 * * ?")
	//@Scheduled(fixedRate  = 1000 * 60) 1분에 1번씩
	@Scheduled(cron = "0 0 0/1 * * *")
	@PostConstruct
	public void searchOnceinHour() {
		if(fetch == null)
			return;
		fetch.startFetch();
	}
	*/
}
