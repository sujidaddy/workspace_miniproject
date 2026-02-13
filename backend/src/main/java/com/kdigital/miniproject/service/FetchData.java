package com.kdigital.miniproject.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdigital.miniproject.domain.FetchLog;
import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishDetail;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.TopLocationDTO;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.persistence.FetchLogRepository;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.TopLocationRepository;
import com.kdigital.miniproject.persistence.WeatherRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FetchData {
	private final FishRepository fishRepo;
	private final LocationRepository locRepo;
	private final WeatherRepository weaRepo;
	private final FishDetailRepository fishDeRepo;
	private final FetchLogRepository logRepo;
	private final TopLocationRepository topRepo;
	
	static public class FetchResult {
		public boolean bSuccess = true;
		public int totalCount = 0;
		public int locationcount = 0;
		public int weathercount = 0;
		public int weatherupdate = 0;
		public int fishcount = 0;
		public int fishupdate = 0;
	}
	
	String[] gubunList = {"갯바위", "선상"};
	int pageNo = 1;
	int dataSize = 0;
	int totalCount = 0;
	
	// 캐시: 데이터베이스 쿼리를 줄이기 위한 맵 구조
	private Map<String, Location> locationCache = new HashMap<>();
	private Map<String, Weather> weatherCache = new HashMap<>();
	private Map<String, Fish> fishCache = new HashMap<>();
	private Set<String> fishDetailCache = new HashSet<>();
	
	// 배치 저장용 리스트
	private List<Location> locationsToSave = new ArrayList<>();
	private List<Weather> weathersToSave = new ArrayList<>();
	private List<Fish> fishToSave = new ArrayList<>();
	private List<FishDetail> fishDetailsToSave = new ArrayList<>();
	
	private static final int BATCH_SIZE = 100;  // 배치 처리 크기
	
	public void startFetch(LocalDateTime date){
		this.pageNo = 1;
		this.dataSize = 0;
		int gubunIndex = 0;
		String Baseurl = "https://apis.data.go.kr/1192136/fcstFishing/GetFcstFishingApiService?serviceKey=2cff2e258e29ed84bb63d34cc6e6f3bb0fddea2816f5c5b9148cb659019c8d03&type=json";
		String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String DateParam = "&reqDate=" + dateStr;
		String GubunParam = "&gubun=" + gubunList[gubunIndex];
		String PageParam = "&pageNo=" + this.pageNo;
		String NumParam = "&numOfRows=300";

		int tot = 0;
		int totLocationcount = 0;
		int totWeathercount = 0;
		int totWeatherupdate = 0;
		int totFishcount = 0;
		int totFishupdate = 0;
		
		// 캐시 초기화
		initializeCache();
		
		String callUrl = Baseurl + DateParam + GubunParam + PageParam + NumParam;
		FetchResult result = startFetch(callUrl, false); 
		while (result.bSuccess)
		{
			totLocationcount += result.locationcount;
			totWeathercount += result.weathercount;
			totWeatherupdate += result.weatherupdate;
			totFishcount += result.fishcount;
			totFishupdate += result.fishupdate;
			
			// 종료 조건
			if(this.dataSize >= tot && gubunIndex == 1)
				break;
			++this.pageNo;
			if(this.dataSize >= tot && gubunIndex == 0)
			{
				this.pageNo = 1;
				++gubunIndex;
				dataSize = 0;
			}
			GubunParam = "&gubun=" + gubunList[gubunIndex];
			PageParam = "&pageNo=" + this.pageNo;
			callUrl = Baseurl + DateParam + GubunParam + PageParam + NumParam;
			result = startFetch(callUrl, false);
		}
		
		// 남은 데이터 배치 저장
		flushBatch();
		
		System.out.println(dateStr + " : " + totLocationcount + "개의 위치와 " + totWeathercount + "개의 날씨와 " + totFishcount + "개의 어종이 추가되었습니다.");
		System.out.println(totWeatherupdate + "개의 날씨와 " + totFishupdate + "개의 어종이 갱신되었습니다.");
		System.out.println("데이터 갱신을 완료 했습니다.");
	}
	
	/**
	 * 캐시 초기화 및 전체 데이터 로드
	 */
	private void initializeCache() {
		// Location 캐시 전체 로드
		locRepo.findAll().forEach(loc -> locationCache.put(loc.getName(), loc));
		
		// FishDetail 캐시 전체 로드
		fishDeRepo.findAll().forEach(detail -> fishDetailCache.add(detail.getName()));
	}
	
	/**
	 * 배치 저장: 모든 대기 중인 데이터를 한 번에 저장
	 */
	private void flushBatch() {
		if (!locationsToSave.isEmpty()) {
			locRepo.saveAll(locationsToSave);
			locationsToSave.clear();
		}
		if (!weathersToSave.isEmpty()) {
			weaRepo.saveAll(weathersToSave);
			weathersToSave.clear();
		}
		if (!fishToSave.isEmpty()) {
			fishRepo.saveAll(fishToSave);
			fishToSave.clear();
		}
		if (!fishDetailsToSave.isEmpty()) {
			fishDeRepo.saveAll(fishDetailsToSave);
			fishDetailsToSave.clear();
		}
	}
	
	@Transactional
	public FetchResult startFetch(String url, boolean bTest) {
		FetchResult result = new FetchResult();
		ResponseEntity<Map<String, Object>> resultMap = fetch(url);
		
		ObjectMapper mapper = new ObjectMapper();
		String errorLog = null;
		try {
			HashMap<String, Object> header = mapper.convertValue(resultMap.getBody().get("header"),
					new TypeReference<>() {
					});
			String resultCode = (String) header.get("resultCode");
			if (!resultCode.equals("00")) {
				result.bSuccess = false;
				return result;
			}

			HashMap<String, Object> body = mapper.convertValue(resultMap.getBody().get("body"), new TypeReference<>() {
			});
			result.totalCount = Integer.parseInt(String.valueOf(body.get("totalCount")));
			pageNo = Integer.parseInt(String.valueOf(body.get("pageNo")));
			HashMap<String, Object> items = mapper.convertValue(body.get("items"), new TypeReference<>() {
			});
			ArrayList<HashMap<String, String>> itemlist = mapper.convertValue(items.get("item"), new TypeReference<>() {
			});
			
			// 배치 저장을 위한 맵으로 중복 제거
			Map<String, Location> locationsMap = new HashMap<>();
			Map<String, Weather> weathersMap = new HashMap<>();
			Map<String, Fish> fishMap = new HashMap<>();
			Set<String> fishDetailsSet = new HashSet<>();
			
			// 1단계: Location 및 Weather 처리
			for (HashMap<String, String> i : itemlist) {
				// 위치 정보 처리
				String seafsPstnNm = i.get("seafsPstnNm");
				Double lat = Double.parseDouble(String.valueOf(i.get("lat")));
				Double lot = Double.parseDouble(String.valueOf(i.get("lot")));
				
				Location loc;
				if (locationCache.containsKey(seafsPstnNm)) {
					loc = locationCache.get(seafsPstnNm);
				} else {
					loc = locationsMap.computeIfAbsent(seafsPstnNm, k -> {
						Location newLoc = Location.builder().name(k).lat(lat).lot(lot).build();
						if(!bTest) locationsToSave.add(newLoc);
						++result.locationcount;
						return newLoc;
					});
				}
				
				// 날씨 정보 처리
				LocalDate predcYmd = null;
				try {
					predcYmd = LocalDate.parse(i.get("predcYmd"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} catch (DateTimeParseException e) {
					// Skip invalid dates
				}
				
				String predcNoonSeCd = i.get("predcNoonSeCd");
				String weatherKey = loc.getName() + "_" + predcYmd + "_" + predcNoonSeCd;
				
				Weather wea;
				if (weatherCache.containsKey(weatherKey)) {
					wea = weatherCache.get(weatherKey);
					updateWeather(wea, i);
					++result.weatherupdate;
				} else if (weathersMap.containsKey(weatherKey)) {
					wea = weathersMap.get(weatherKey);
					updateWeather(wea, i);
					++result.weatherupdate;
				} else {
					Double minWvhgt = Double.parseDouble(String.valueOf(i.get("minWvhgt")));
					Double maxWvhgt = Double.parseDouble(String.valueOf(i.get("maxWvhgt")));
					Double minWtem = Double.parseDouble(String.valueOf(i.get("minWtem")));
					Double maxWtem = Double.parseDouble(String.valueOf(i.get("maxWtem")));
					Double minArtmp = Double.parseDouble(String.valueOf(i.get("minArtmp")));
					Double maxArtmp = Double.parseDouble(String.valueOf(i.get("maxArtmp")));
					Double minCrsp = Double.parseDouble(String.valueOf(i.get("minCrsp")));
					Double maxCrsp = Double.parseDouble(String.valueOf(i.get("maxCrsp")));
					Double minWspd = Double.parseDouble(String.valueOf(i.get("minWspd")));
					Double maxWspd = Double.parseDouble(String.valueOf(i.get("maxWspd")));
					
					wea = Weather.builder().predcYmd(predcYmd).predcNoonSeCd(predcNoonSeCd).minWvhgt(minWvhgt)
							.maxWvhgt(maxWvhgt).minWtem(minWtem).maxWtem(maxWtem).minArtmp(minArtmp).maxArtmp(maxArtmp)
							.minCrsp(minCrsp).maxCrsp(maxCrsp).minWspd(minWspd).maxWspd(maxWspd).location(loc).build();
					weathersToSave.add(wea);
					weathersMap.put(weatherKey, wea);
					++result.weathercount;
				}
			}
			
			// Weather를 먼저 저장 (ID 생성을 위해)
			if (!weathersToSave.isEmpty() && !bTest) {
				weaRepo.saveAll(weathersToSave);
				weathersToSave.forEach(w -> weatherCache.put(
					w.getLocation().getName() + "_" + w.getPredcYmd() + "_" + w.getPredcNoonSeCd(), w));
				weathersToSave.clear();
			}
			
			// 2단계: Fish 처리 (이제 Weather의 ID가 있음)
			for (HashMap<String, String> i : itemlist) {
				String seafsPstnNm = i.get("seafsPstnNm");
				Double lat = Double.parseDouble(String.valueOf(i.get("lat")));
				Double lot = Double.parseDouble(String.valueOf(i.get("lot")));
				
				Location loc;
				if (locationCache.containsKey(seafsPstnNm)) {
					loc = locationCache.get(seafsPstnNm);
				} else {
					loc = locationsMap.get(seafsPstnNm);
				}
				
				LocalDate predcYmd = null;
				try {
					predcYmd = LocalDate.parse(i.get("predcYmd"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} catch (DateTimeParseException e) {
					// Skip invalid dates
				}
				
				String predcNoonSeCd = i.get("predcNoonSeCd");
				String weatherKey = loc.getName() + "_" + predcYmd + "_" + predcNoonSeCd;
				Weather wea = weatherCache.containsKey(weatherKey) ? weatherCache.get(weatherKey) : weathersMap.get(weatherKey);
				
				// 어종 정보 처리
				String seafsTgfshNm = i.get("seafsTgfshNm");
				if (seafsTgfshNm == null || seafsTgfshNm.length() == 0)
					seafsTgfshNm = "선상낚시";
				
				Double tdlvHrScr = Double.parseDouble(String.valueOf(i.get("tdlvHrScr")));
				String totalIndex = i.get("totalIndex");
				Double lastScr = Double.parseDouble(String.valueOf(i.get("lastScr")));
				String fishKey = loc.getName() + "_" + wea.getWeather_no() + "_" + seafsTgfshNm;
				
				Fish fish;
				if (fishMap.containsKey(fishKey)) {
					fish = fishMap.get(fishKey);
					fish.setTdlvHrScr(tdlvHrScr);
					fish.setTotalIndex(totalIndex);
					fish.setLastScr(lastScr);
					++result.fishupdate;
				} else {
					fish = Fish.builder().name(seafsTgfshNm).tdlvHrScr(tdlvHrScr).totalIndex(totalIndex)
							.lastScr(lastScr).weather(wea).location(loc).build();
					fishToSave.add(fish);
					fishMap.put(fishKey, fish);
					++result.fishcount;
				}
				
				// 새로운 어종 등록
				if (fishDetailCache.contains(seafsTgfshNm) == false) {
					FishDetail detail = FishDetail.builder().name(seafsTgfshNm).build();
					fishDetailsToSave.add(detail);
					fishDetailCache.add(seafsTgfshNm);
				}
			}
			
			// Fish 배치 저장
			if (!fishToSave.isEmpty() && !bTest) {
				fishRepo.saveAll(fishToSave);
				fishToSave.clear();
			}
			
			// FishDetail 배치 저장
			if (!fishDetailsToSave.isEmpty() && !bTest) {
				fishDeRepo.saveAll(fishDetailsToSave);
				fishDetailsToSave.forEach(d -> fishDetailCache.add(d.getName()));
				fishDetailsToSave.clear();
			}
			
			// Location 배치 저장 (마지막에)
			if (!locationsToSave.isEmpty() && !bTest) {
				locRepo.saveAll(locationsToSave);
				locationsToSave.forEach(loc -> locationCache.put(loc.getName(), loc));
				locationsToSave.clear();
			}
			
			this.dataSize += itemlist.size();
			
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			errorLog = sw.toString();
		} finally {
			// 로그 추가
			if(!bTest)
				logRepo.save(FetchLog.builder().fetchUrl(url).errorMsg(errorLog).locationCount(result.locationcount)
						.weatherCount(result.weathercount).weatherUpdate(result.weatherupdate).fishCount(result.fishcount)
						.fishUpdate(result.fishupdate).build());
		}
		return result;
	}
	
	/**
	 * 날씨 정보 업데이트 헬퍼 메서드
	 */
	private void updateWeather(Weather wea, HashMap<String, String> data) {
		wea.setMinWvhgt(Double.parseDouble(String.valueOf(data.get("minWvhgt"))));
		wea.setMaxWvhgt(Double.parseDouble(String.valueOf(data.get("maxWvhgt"))));
		wea.setMinWtem(Double.parseDouble(String.valueOf(data.get("minWtem"))));
		wea.setMaxWtem(Double.parseDouble(String.valueOf(data.get("maxWtem"))));
		wea.setMinArtmp(Double.parseDouble(String.valueOf(data.get("minArtmp"))));
		wea.setMaxArtmp(Double.parseDouble(String.valueOf(data.get("maxArtmp"))));
		wea.setMinCrsp(Double.parseDouble(String.valueOf(data.get("minCrsp"))));
		wea.setMaxCrsp(Double.parseDouble(String.valueOf(data.get("maxCrsp"))));
		wea.setMinWspd(Double.parseDouble(String.valueOf(data.get("minWspd"))));
		wea.setMaxWspd(Double.parseDouble(String.valueOf(data.get("maxWspd"))));
	}
	
	public void fetchTop3(LocalDate curDate) {
		List<TopLocationDTO> haveList =  topRepo.findByCreateDate(curDate);
		//System.out.println("curDate : " + curDate);
		//System.out.println("size : " + haveList.size());
		if(haveList.size() > 0)
			return;
		int topCount = 3;
		List<Fish> addList = fishRepo.findFishPointTop(topCount);
		for(Fish f : addList) {
			topRepo.save(new TopLocationDTO(f));
		}
	}
	
	public ResponseEntity<Map<String, Object>> fetch(String url) {
		try
		{
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
			ResponseEntity<Map<String, Object>> resultMap 
				= restTemplate.exchange(
						url, 
						HttpMethod.GET, 
						entity, 
						new ParameterizedTypeReference<Map<String, Object>>() {
						});
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
