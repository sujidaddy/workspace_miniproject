package com.kdigital.miniproject.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FetchData {
	private final FishService fishService;
	private final LocationService locService;
	
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
			for(HashMap<String, String> i : (ArrayList<HashMap<String, String>>)item) {
				// 위치 정보
				String seafsPstnNm = i.get("seafsPstnNm");
				Double lat = Double.parseDouble(String.valueOf(i.get("lat")));
				Double lot = Double.parseDouble(String.valueOf(i.get("lot")));
				Location loc = locService.getLocation(seafsPstnNm);
				if(loc == null) {
					loc = Location.builder()
						.name(seafsPstnNm)
						.lat(lat)
						.lot(lot)
						.build();
					locService.insertLocation(loc);
				}
				// 어종 정보
				String seafsTgfshNm = i.get("seafsTgfshNm");
				Double tdvHrScr = Double.parseDouble(String.valueOf(i.get("tdlvHrScr")));
				String totalIndex = i.get("totalIndex");
				Double lastScr = Double.parseDouble(String.valueOf(i.get("lastScr")));
//				Fish newFish = Fish.builder()
//									.name(seafsTgfshNm)
//									.tdvHrScr(tdvHrScr)
//									.totalIndex(totalIndex)
//									.lastScr(lastScr)
//									.build();
//				fishService.insertFish(newFish);
				
			}
			
			
			this.dataSize += numOfRows;
			System.out.println("dataSize : " + this.dataSize + ", tot : " + tot);
			if(this.dataSize > tot && gubunIndex == 1)
				break;			
			++pageNo;
			if(this.dataSize > tot && gubunIndex == 0)
			{
				pageNo = 1;
				gubunIndex = 0;
				dataSize = 0;
			}
			GubunParam = "&gubun=" + gubunList[gubunIndex];
			PageParam = "&pageNo=" + pageNo;
			callUrl = Baseurl + DateParam + GubunParam + PageParam + NumParam;
			resultMap = (ResponseEntity<Map>)fetch(callUrl);
			
		}
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
