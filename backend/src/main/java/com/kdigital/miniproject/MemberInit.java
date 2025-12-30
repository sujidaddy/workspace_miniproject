package com.kdigital.miniproject;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.FishDetail;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.AreaRepository;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberInit implements ApplicationRunner{
	private final MemberRepository memberRepo;
	private final AreaRepository areaRepo;
	private final FishDetailRepository fishDRepo;
	private PasswordEncoder encoder = new PasswordEncoder();
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		Optional<Member> admin =memberRepo.getByUserid("administrator");  
		if(admin.isEmpty())
		{
			memberRepo.save(Member.builder()
				.userid("administrator")
				.password(encoder.encode("Abcd1234!@"))
				.username("관리자")
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
		
		if(fishDRepo.getByName("감성돔") == null)
			fishDRepo.save(FishDetail.builder()
					.name("감성돔")
					.detail("도미과에 속하는 해수어이다. 감성돔이라는 이름은 검은 돔에서 변화한 것으로 추정된다. 감상어, 먹도미, 감성도미, 감셍이, 구릿, 맹이, 남정바리 등으로 불리기도 한다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Black_seabream%28side.JPG/250px-Black_seabream%28side.JPG")
					.build());
		
		if(fishDRepo.getByName("기타어종") == null)
			fishDRepo.save(FishDetail.builder()
					.name("기타어종")
					.detail("바닷물고기 또는 해수어(海水魚)는 바다에서 사는 어류를 말한다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Georgia_Aquarium_-_Giant_Grouper_edit.jpg/250px-Georgia_Aquarium_-_Giant_Grouper_edit.jpg")
					.build());
		if(fishDRepo.getByName("농어") == null)
			fishDRepo.save(FishDetail.builder()
					.name("농어")
					.detail(" 주걱치목 농어과에 속하는 물고기이다. 농어목으로 분류하기도 한다. 최근 심해 탐험중 밝혀진 결과로는 몸길이 최대 2m가 넘는다고 한다. 몸은 약간 길고 납작하며 등은 푸른빛이 도는 회색, 배는 은백색이다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Suzuki201302.jpg/250px-Suzuki201302.jpg")
					.build());
		if(fishDRepo.getByName("돌돔") == null)
			fishDRepo.save(FishDetail.builder()
					.name("돌돔")
					.detail("검정우럭목 돌돔과에 속하는 대형 육식 물고기이다.[1] 식용이나 낚시 대상으로 인기가 많다. 농어목으로 분류하기도 한다. 한국, 일본 연해에 많이 분포한다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/K231003%EB%8F%8C%EB%8F%94.jpg/250px-K231003%EB%8F%8C%EB%8F%94.jpg")
					.build());
		if(fishDRepo.getByName("우럭") == null)
			fishDRepo.save(FishDetail.builder()
					.name("우럭")
					.detail("쏨뱅이목 양볼락과에 속하는, 아시아 북부에서 볼 수 있는 바닷물고기이다. 어린 것의 몸색은 검은색이며 나이가 듦에 따라 측면에 얼룰덜룩한 회색으로 바뀌면서 흰색에 가까워진다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Sebastes_schlegelii_Hilgendorf%2C_1880.jpg/250px-Sebastes_schlegelii_Hilgendorf%2C_1880.jpg")
					.build());
		if(fishDRepo.getByName("참돔") == null)
			fishDRepo.save(FishDetail.builder()
					.name("참돔")
					.detail("도미과의 물고기이다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Pagrus_major_Red_seabream_ja01.jpg/250px-Pagrus_major_Red_seabream_ja01.jpg")
					.build());
		if(fishDRepo.getByName("벵어돔") == null)
			fishDRepo.save(FishDetail.builder()
					.name("벵어돔")
					.detail("벵에돔과에 속하는 물고기로 동아시아의 온대 바다에 서식한다.[1]몸길이는 60cm이다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/%E3%83%A1%E3%82%B8%E3%83%8A_%2815257760119%29.jpg/250px-%E3%83%A1%E3%82%B8%E3%83%8A_%2815257760119%29.jpg")
					.build());
		if(fishDRepo.getByName("볼락") == null)
			fishDRepo.save(FishDetail.builder()
					.name("볼락")
					.detail("페르카목 쏨뱅이과에 속하는 조기어류의 일종이다. 몸길이 20-30cm이다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Sebastes_Inermis.jpg/250px-Sebastes_Inermis.jpg")
					.build());
		if(fishDRepo.getByName("열기") == null)
			fishDRepo.save(FishDetail.builder()
					.name("열기")
					.detail("불볼락은 일명 열기로 불리기도 하는데, 그물과 주낙으로 주로 잡는 어종이다. 매운탕과 회로도 먹지만, 내장과 뼈를 제거하고, 해수에 염장한 뒤 해풍에 말려 구워먹기도 한다.")
					.url("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Sebastes_thompsoni.JPG/250px-Sebastes_thompsoni.JPG")
					.build());
		
	}

}
