package com.kdigital.miniproject.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.kdigital.miniproject.service.FishService;
import com.kdigital.miniproject.service.LocationService;
import com.kdigital.miniproject.service.WeatherService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FetchData {
	private final FishService fishService;
	private final LocationService locService;
	private final WeatherService weaService;
	
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
		ResponseEntity<Map> resultMap = (ResponseEntity<Map>)fetch(callUrl);
		int locationcount = 0;
		int weathercount = 0;
		int weatherupdate = 0;
		int fishcount = 0;
		int fishupdate = 0;
		while(resultMap != null) {
			//System.out.println("url = " + callUrl);
			HashMap<String, String> header = (HashMap<String, String>)resultMap.getBody().get("header");
			String resultCode = header.get("resultCode");
			String resultMsg = header.get("resultMsg");
			if(!resultCode.equals("00"))
				return;
			JSONObject body = new JSONObject((HashMap<String, String>)resultMap.getBody().get("body"));
			//System.out.println(body.toString());
			int tot = Integer.parseInt(String.valueOf(body.get("totalCount")));
			//int pageNo = Integer.parseInt(String.valueOf(body.get("pageNo")));
			int numOfRows = Integer.parseInt(String.valueOf(body.get("numOfRows")));
			Object item = ((HashMap<String, String>)body.get("items")).get("item");
			ArrayList<HashMap<String, String>> itemlist = (ArrayList<HashMap<String, String>>)item; 
			for(HashMap<String, String> i : itemlist) {
				// 위치 정보
				String seafsPstnNm = i.get("seafsPstnNm");
				Double lat = Double.parseDouble(String.valueOf(i.get("lat")));
				Double lot = Double.parseDouble(String.valueOf(i.get("lot")));
				Location loc = locService.getLocationByName(seafsPstnNm);
				if(loc == null) {
					loc = Location.builder()
						.name(seafsPstnNm)
						.lat(lat)
						.lot(lot)
						.build();
					locService.insertLocation(loc);
					++locationcount;
				}
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
				Weather wea = weaService.getWeather(loc, predcYmd, predcNoonSeCd); 
				if(wea == null) {
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
					weaService.insertWeather(wea);
					++weathercount;
				} else {
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
				// 어종 정보
				String seafsTgfshNm = i.get("seafsTgfshNm");
				Double tdvHrScr = Double.parseDouble(String.valueOf(i.get("tdlvHrScr")));
				String totalIndex = i.get("totalIndex");
				Double lastScr = Double.parseDouble(String.valueOf(i.get("lastScr")));
				Fish fish = fishService.getFish(loc, wea, seafsTgfshNm); 
				if(fish == null)
				{
					fish = Fish.builder()
										.name(seafsTgfshNm)
										.tdvHrScr(tdvHrScr)
										.totalIndex(totalIndex)
										.lastScr(lastScr)
										.weather(wea)
										.location(loc)
										.build();
					fishService.insertFish(fish);
					++fishcount;
				} else {
					fish.setTdvHrScr(tdvHrScr);
					fish.setTotalIndex(totalIndex);
					fish.setLastScr(lastScr);
					++fishupdate;
				}
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
			resultMap = (ResponseEntity<Map>)fetch(callUrl);
		}
		System.out.println(reqDate + " : " + locationcount + "개의 위치와 " + weathercount + "개의 날씨와 " + fishcount + "개의 어종이 추가되었습니다.");
		System.out.println(weatherupdate + "개의 날씨와 " + fishupdate + "개의 어종이 갱신되었습니다.");
	}
	
	public ResponseEntity<Map> fetch(String url) {
		try
		{
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
			ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
