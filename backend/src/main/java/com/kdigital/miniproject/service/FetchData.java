package com.kdigital.miniproject.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
	
	String[] gubunList = {"갯바위", "선상"};
	int pageNo = 1;
	LocalDate reqDate;
	int dataSize = 0;
	int totalCount = 0;
	
	public void setDate(LocalDate date) {
		reqDate = date;
	}
	
	public void startFetch(){
		pageNo = 1;
		if (reqDate == null)
			reqDate = LocalDate.now();
		dataSize = 0;
		int gubunIndex = 0;
		String Baseurl = "https://apis.data.go.kr/1192136/fcstFishing/GetFcstFishingApiService?serviceKey=2cff2e258e29ed84bb63d34cc6e6f3bb0fddea2816f5c5b9148cb659019c8d03&type=json";
		//SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		//String DateParam = "&reqDate=" + format.format(reqDate);
		String DateParam = "&reqDate=" + reqDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String GubunParam = "&gubun=" + gubunList[gubunIndex];
		String PageParam = "&pageNo=" + pageNo;
		String NumParam = "&numOfRows=300";
		
		String callUrl = Baseurl + DateParam + GubunParam + PageParam + NumParam;
		ResponseEntity<Map<String, Object>> resultMap = fetch(callUrl);
		int tot = 0;
		int pageNo = 0;
		int totLocationcount = 0;
		int totWeathercount = 0;
		int totWeatherupdate = 0;
		int totFishcount = 0;
		int totFishupdate = 0;
		ObjectMapper mapper = new ObjectMapper();
		while(resultMap != null) {
			int locationcount = 0;
			int weathercount = 0;
			int weatherupdate = 0;
			int fishcount = 0;
			int fishupdate = 0;
			String errorLog = null;
			try {
				//System.out.println("url = " + callUrl);
				HashMap<String, Object> header = mapper.convertValue(resultMap.getBody().get("header"), new TypeReference<>() {});
				//System.out.println("header = " + header);
				//String resultCode = header.get("resultCode");
				String resultCode = (String)header.get("resultCode");
				//String resultMsg = (String)header.get("resultMsg");
				if(!resultCode.equals("00"))
					return;
				
				HashMap<String, Object> body = mapper.convertValue(resultMap.getBody().get("body"), new TypeReference<>() {});
				//System.out.println(body);
				tot = Integer.parseInt(String.valueOf(body.get("totalCount")));
				pageNo = Integer.parseInt(String.valueOf(body.get("pageNo")));
				//int numOfRows = Integer.parseInt(String.valueOf(body.get("numOfRows")));
				//System.out.println(tot + ", " + pageNo + ", " + numOfRows);
				HashMap<String, Object> items = mapper.convertValue(body.get("items"), new TypeReference<>() {});
				//System.out.println(items);
				ArrayList<HashMap<String, String>> itemlist = mapper.convertValue(items.get("item"), new TypeReference<>() {});
				//System.out.println(itemlist);
				for(HashMap<String, String> i : itemlist) {
					// 위치 정보
					String seafsPstnNm = i.get("seafsPstnNm");
					Double lat = Double.parseDouble(String.valueOf(i.get("lat")));
					Double lot = Double.parseDouble(String.valueOf(i.get("lot")));
					Optional<Location> locOpt = locRepo.findByName(seafsPstnNm);
					Location loc;
					if(locOpt.isEmpty()) {
						loc = Location.builder()
							.name(seafsPstnNm)
							.lat(lat)
							.lot(lot)
							.build();
						locRepo.save(loc);
						++locationcount;
					}
					else 
						loc = locOpt.get();
					// 날씨 정보
					//SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
					LocalDate predcYmd = null;
					try {
						predcYmd = LocalDate.parse(i.get("predcYmd"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
					} catch (DateTimeParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String predcNoonSeCd = i.get("predcNoonSeCd");
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
					
					
					Optional<Weather> weaOpt = weaRepo.findByLocationAndPredcYmdAndPredcNoonSeCd(loc, predcYmd, predcNoonSeCd);
					Weather wea;
					if(weaOpt.isEmpty()) {
						wea = Weather.builder()
								.predcYmd(predcYmd)
								.predcNoonSeCd(predcNoonSeCd)
								.minWvhgt(minWvhgt)
								.maxWvhgt(maxWvhgt)
								.minWtem(minWtem)
								.maxWtem(maxWtem)
								.minArtmp(minArtmp)
								.maxArtmp(maxArtmp)
								.minCrsp(minCrsp)
								.maxCrsp(maxCrsp)
								.minWspd(minWspd)
								.maxWspd(maxWspd)
								.location(loc)
								.build();
						++weathercount;
					} else {
						wea = weaOpt.get();
						wea.setMinWvhgt(minWvhgt);
						wea.setMaxWvhgt(maxWvhgt);
						wea.setMinWtem(minWtem);
						wea.setMaxWtem(maxWtem);
						wea.setMinArtmp(minArtmp);
						wea.setMaxArtmp(maxArtmp);
						wea.setMinCrsp(minCrsp);
						wea.setMaxCrsp(maxCrsp);
						wea.setMinWspd(minWspd);
						wea.setMaxWspd(maxWspd);
						++weatherupdate;
					}
					weaRepo.save(wea);
					// 어종 정보
					String seafsTgfshNm = i.get("seafsTgfshNm");
					if(seafsTgfshNm == null || seafsTgfshNm.length() == 0)
						seafsTgfshNm = "바다낚시";
					Double tdlvHrScr = Double.parseDouble(String.valueOf(i.get("tdlvHrScr")));
					String totalIndex = i.get("totalIndex");
					Double lastScr = Double.parseDouble(String.valueOf(i.get("lastScr")));
					Optional<Fish> fishOpt = fishRepo.findByLocationAndWeatherAndName(loc, wea, seafsTgfshNm); 
					Fish fish;
					if(fishOpt.isEmpty())
					{
						fish = Fish.builder()
							.name(seafsTgfshNm)
							.tdlvHrScr(tdlvHrScr)
							.totalIndex(totalIndex)
							.lastScr(lastScr)
							.weather(wea)
							.location(loc)
							.build();
						++fishcount;
					} else {
						fish = fishOpt.get();
						fish.setTdlvHrScr(tdlvHrScr);
						fish.setTotalIndex(totalIndex);
						fish.setLastScr(lastScr);
						++fishupdate;
					}
					fishRepo.save(fish);
					
					// 등록되어 있지 않은 어종이 추가되면
					if(seafsTgfshNm != null && seafsTgfshNm.length() > 0 && fishDeRepo.findByName(seafsTgfshNm).isEmpty()) {
						FishDetail detail = FishDetail.builder()
											.name(seafsTgfshNm)
											.build();
						fishDeRepo.save(detail);
					}
				}
				this.dataSize += itemlist.size();
				//System.out.println("dataSize : " + this.dataSize + ", tot : " + tot);
			} catch(Exception e) {
				e.printStackTrace();
				StringWriter sw = new StringWriter();
			    e.printStackTrace(new PrintWriter(sw));
				errorLog = sw.toString();
				break;
			} finally {
				// 로그 추가
				logRepo.save(FetchLog.builder().fetchUrl(callUrl)
					.errorMsg(errorLog)
					.locationCount(locationcount)
					.weatherCount(weathercount)
					.weatherUpdate(weatherupdate)
					.fishCount(fishcount)
					.fishUpdate(fishupdate)
					.build());
			}
			
			totLocationcount += locationcount;
			totWeathercount += weathercount;
			totWeatherupdate += weatherupdate;
			totFishcount += fishcount;
			totFishupdate += fishupdate;
			// 로그 추가

			// 종료 조건
			if(this.dataSize >= tot && gubunIndex == 1)
				break;
			++pageNo;
			if(this.dataSize >= tot && gubunIndex == 0)
			{
				pageNo = 1;
				++gubunIndex;
				dataSize = 0;
			}
			GubunParam = "&gubun=" + gubunList[gubunIndex];
			PageParam = "&pageNo=" + pageNo;
			callUrl = Baseurl + DateParam + GubunParam + PageParam + NumParam;
			resultMap = fetch(callUrl);
		}
		System.out.println(reqDate + " : " + totLocationcount + "개의 위치와 " + totWeathercount + "개의 날씨와 " + totFishcount + "개의 어종이 추가되었습니다.");
		System.out.println(totWeatherupdate + "개의 날씨와 " + totFishupdate + "개의 어종이 갱신되었습니다.");
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
