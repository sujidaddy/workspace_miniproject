package com.kdigital.miniproject.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.WeatherRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FetchData {
	private final FishRepository fishRepo;
	private final LocationRepository locRepo;
	private final WeatherRepository weaRepo;
	
	String[] gubunList = {"갯바위", "선상"};
	int pageNo = 1;
	Date reqDate;
	int dataSize = 0;
	int totalCount = 0;
	
	public void setDate(Date date) {
		reqDate = date;
	}
	
	public void startFetch(){
		pageNo = 1;
		if (reqDate == null)
			reqDate = new Date();
		dataSize = 0;
		int gubunIndex = 0;
		String Baseurl = "https://apis.data.go.kr/1192136/fcstFishing/GetFcstFishingApiService?serviceKey=2cff2e258e29ed84bb63d34cc6e6f3bb0fddea2816f5c5b9148cb659019c8d03&type=json";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String DateParam = "&reqDate=" + format.format(reqDate);
		String GubunParam = "&gubun=" + gubunList[gubunIndex];
		String PageParam = "&pageNo=" + pageNo;
		String NumParam = "&numOfRows=300";
		
		String callUrl = Baseurl + DateParam + GubunParam + PageParam + NumParam;
		ResponseEntity<Map<String, Object>> resultMap = fetch(callUrl);
		int locationcount = 0;
		int weathercount = 0;
		int weatherupdate = 0;
		int fishcount = 0;
		int fishupdate = 0;
		ObjectMapper mapper = new ObjectMapper();
		while(resultMap != null) {
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
			int tot = Integer.parseInt(String.valueOf(body.get("totalCount")));
			int pageNo = Integer.parseInt(String.valueOf(body.get("pageNo")));
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
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
				Date predcYmd = null;
				try {
					predcYmd = format2.parse(i.get("predcYmd"));
				} catch (ParseException e) {
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
				Double tdvHrScr = Double.parseDouble(String.valueOf(i.get("tdlvHrScr")));
				String totalIndex = i.get("totalIndex");
				Double lastScr = Double.parseDouble(String.valueOf(i.get("lastScr")));
				Optional<Fish> fishOpt = fishRepo.findByLocationAndWeatherAndName(loc, wea, seafsTgfshNm); 
				Fish fish;
				if(fishOpt.isEmpty())
				{
					fish = Fish.builder()
						.name(seafsTgfshNm)
						.tdvHrScr(tdvHrScr)
						.totalIndex(totalIndex)
						.lastScr(lastScr)
						.weather(wea)
						.location(loc)
						.build();
					++fishcount;
				} else {
					fish = fishOpt.get();
					fish.setTdvHrScr(tdvHrScr);
					fish.setTotalIndex(totalIndex);
					fish.setLastScr(lastScr);
					++fishupdate;
				}
				fishRepo.save(fish);
			}
			
			this.dataSize += itemlist.size();
			//System.out.println("dataSize : " + this.dataSize + ", tot : " + tot);
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
		System.out.println(reqDate + " : " + locationcount + "개의 위치와 " + weathercount + "개의 날씨와 " + fishcount + "개의 어종이 추가되었습니다.");
		System.out.println(weatherupdate + "개의 날씨와 " + fishupdate + "개의 어종이 갱신되었습니다.");
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
