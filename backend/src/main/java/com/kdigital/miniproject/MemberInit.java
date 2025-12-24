package com.kdigital.miniproject;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.AreaRepository;
import com.kdigital.miniproject.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberInit implements ApplicationRunner{
	private final MemberRepository memberRepo;
	private final AreaRepository areaRepo;
	private PasswordEncoder encoder = new PasswordEncoder();
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		if(memberRepo.findById(0L).isEmpty())
		{
			memberRepo.save(Member.builder()
				.user_no(0)
				.userid("admin")
				.password(encoder.encode("abcd"))
				.username("admin")
				.email("admin@miniproject.kdigital.com")
				.role(Role.ROLE_ADMIN)
				.enabled(true)
				.createTime(LocalDateTime.of(2025, 12, 1, 0, 0, 0))
				.lastLoginTime(LocalDateTime.now())
				.build());
		}
		
		areaRepo.save(Area.builder()
				.area_no(1)
				.area_name("경기권")
				.center_lat(36.898757)
				.center_lng(126.076931)
				.radius(60000)
				.level((short)11)
				.fillColor("#FF6B6B")
				.build());
		
		areaRepo.save(Area.builder()
				.area_no(2)
				.area_name("전라권")
				.center_lat(34.928493)
				.center_lng(126.318448)
				.radius(140000)
				.level((short)11)
				.fillColor("#4ECDC4")
				.build());
		
		areaRepo.save(Area.builder()
				.area_no(3)
				.area_name("경상권")
				.center_lat(35.599159)
				.center_lng(129.198111)
				.radius(140000)
				.level((short)11)
				.fillColor("#45B7D1")
				.build());
		
		areaRepo.save(Area.builder()
				.area_no(4)
				.area_name("강원권")
				.center_lat(37.79245)
				.center_lng(128.856412)
				.radius(80000)
				.level((short)11)
				.fillColor("#F9C74F")
				.build());
		
		areaRepo.save(Area.builder()
				.area_no(5)
				.area_name("제주권")
				.center_lat(33.400526)
				.center_lng(126.541543)
				.radius(30000)
				.level((short)11)
				.fillColor("#9D6B84")
				.build());
	}

}
