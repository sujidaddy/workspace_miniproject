package com.kdigital.miniproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.kdigital.miniproject.domain.FetchData;

@EnableScheduling
@SpringBootApplication
public class BackendApplication {
	
	@Autowired
	private FetchData fetch;
	
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	
	//@Scheduled(cron = "45 21 10 * * ?")
	@Scheduled(fixedRate  = 10000)
	public void searchOnceinDay() {
		if(fetch == null)
			return;
		fetch.startFetch();
	}

}
