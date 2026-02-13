package com.kdigital.miniproject.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.kdigital.miniproject.domain.TopLocationDTO;
import com.kdigital.miniproject.persistence.TopLocationRepository;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class FetchScheduler {
	private final FetchData fetch;
	private final TopLocationRepository topRepo;
	
	// 실행시 동작
	//@Bean
	// 매일 1시에 동작
	//@Scheduled(cron = "0 0 1 * * *")
	// 매 짝수 시에 동작
	//@Scheduled(cron = "0 0 0/2 * * *")
	// 매일 10시 14시에 동작
	@Scheduled(cron = "0 0 10,14 * * *")
	public void fetchStart() {
		if(fetch == null)
			return ;
		boolean fetchAll = true;
		if(fetchAll)
		{
			LocalDateTime now = LocalDateTime.now();
			for(int i = 0; i < 7; ++i) {
				fetch.startFetch(now.plusDays(i));
			}
		}
		List<TopLocationDTO> list =  topRepo.findByCreateDate(LocalDate.now());
		if(list.size() == 0)
			fetch.fetchTop3(LocalDate.now());
	}
}
