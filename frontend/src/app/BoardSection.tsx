// 0114 교수님 수정본 중요  삭제 금지
"use client";
// import { atom, useAtom } from 'jotai'
import { motion } from "framer-motion";
import Link from "next/link";
import KpiSection from "../components/KpiSection";
import ChartPage from "@/app/dashboard/ChartPage";
import { useState, useEffect, useMemo } from "react";
import { fetchFishNameSearch } from "@/ApiList";
import { fetchFishLocationSearch } from "@/ApiList";
import { count } from 'console';
import KakaoMapArea from "./KakaoMap/KakaoMapArea";

interface Area {
  area_no: number;
  area_name: string;
  center_lat: number;
  center_lng: number;
  radius: number;
  fillColor: string;
  level: number;
}
interface Location {
  location_no: number;
  name: string;
  lat: number;
  lng: number;
  area_no: number;
  area_name: string;
}
interface Fish {
  data_no: number;
  name: string;
  tdlvHrScr: number;
  totalIndex: string;
  lastScr: number;
  predcYmd: string;
  predcNoonSeCd: string;
  minWvhgt: number;
  maxWvhgt: number;
  minWtem: number;
  maxWtem: number;
  minArtmp: number;
  maxArtmp: number;
  minCrsp: number;
  maxCrsp: number;
  minWspd: number;
  maxWspd: number;
  lat: number;
  lot: number;
  location_no: number;
}

export default function BoardSection() {
  // State 관리
  const [areas, setAreas] = useState<Area[]>([]); // 권역 목록
  const [locations, setLocations] = useState<Location[]>([]); // 지역 목록
  const [selectedArea, setSelectedArea] = useState(""); // 선택된 권역
  const [selectedLocation, setSelectedLocation] = useState(""); // 선택된 지역
  const [chartData, setChartData] = useState([]);   // 차트에 사용할 데이터

  //지도용
  const [selectedFish, setSelectedFish] = useState<string>("")
  const [selectedMapArea, setSelectedMapArea] = useState("")
  const [fishList, setFishList] = useState<Fish[]>([]); // 어종 목록 추가
  const [fishLocationData, setFishLocationData] = useState<Location[]>([]);
  const [fishDateGroup, setFishDateGroup] = useState<string[]>([]);
  const [selectedFishDate, setSelectedFishDate] = useState<string>("");


  // 2. 컴포넌트 마운트 시 권역(area) 데이터 가져오기
  const fetchFishList = async () => {
    try {
      const response = await fetch("http://10.125.121.176:8080/api/v1/fishlist");
      const result = await response.json();
      console.log("어종 리스트", result.data);

      // API 응답 구조에 맞게 조정
      if (result.data && result.data.length > 0) {
        setFishList(result.data);
        // 첫 번째 어종명을 문자열로 설정
        setSelectedFish(result.data[0].fish_name || "");
      }
    } catch (err) {
      console.error("어종 리스트 로드 실패:", err);
    }
  };

  useEffect(() => {
    fetch("http://10.125.121.176:8080/api/v1/area")
      .then(res => res.json())
      .then(data => {
        console.log("권역 데이터", data.data);
        setAreas(data.data);
        setSelectedArea(data.data[0].area_no);
      })
      .catch(err => console.error("권역 데이터 로드 실패:", err));
  }, []);
  // 3. 권역>> 지역넘기는 것 권역area가 변경될 때마다 실행
  const getLocationData = async (area_no: number) => {
    fetch(`http://10.125.121.176:8080/api/v1/location/area?area_no=${area_no}`)
      .then(res => res.json())
      .then(data => {
        console.log("지역 데이터", data.data);
        setLocations(data.data);
        //setSelectedLocation(data.data[0].location_no);
      })
      .catch(err => console.error("권역 데이터 로드 실패:", err));
  }

  useEffect(() => {
    fetchFishList();
    if (!selectedArea) {
      return;
    }
    console.log("selectedArea", selectedArea);
    getLocationData(Number(selectedArea));

  }, [selectedArea]);
  // 3. 시군구(selectedSgg)가 변경될 때마다 실행

  useEffect(() => {
    if (!selectedLocation) {
      return;
    }

    console.log("!!!!")

  }, [selectedLocation]);

  // [수정 1] 어종 선택 시 -> 날짜 목록 7개 가져오기 (이미지 4번 API)
  useEffect(() => {
    if (!selectedFish) {
      setFishDateGroup([]);
      return;
    }

    const getFishDates = async () => {
      try {
        // 이미지 4번 명세서의 URI: /api/v1/fish/fishDate?fish_name=물고기이름
        const response = await fetch(`http://10.125.121.176:8080/api/v1/fish/fishDate?fish_name=${selectedFish}`);
        const result = await response.json();

        if (result.data) {
          // 날짜 텍스트(weather_date)만 추출하여 콤보박스용 배열 생성
          const dates = result.data.map((item: any) => item.weather_date);
          setFishDateGroup(dates);

          // 날짜가 로드되면 첫 번째 날짜를 자동으로 선택 상태로 만듦
          if (dates.length > 0) setSelectedFishDate(dates[0]);
        }
      } catch (err) {
        console.error("어종별 날짜 로드 실패:", err);
      }
    };

    getFishDates();
  }, [selectedFish]); // 어종(selectedFish)이 바뀔 때만 실행

  useEffect(() => {
    // 어종과 날짜가 모두 있어야만 실행
    if (!selectedFish || !selectedFishDate) return;

    const getMapData = async () => {
      try {
        // 이미지 5번 명세서의 URI: /api/v1/fish/fishListByDate?fish_name=...&weather_date=...
        const response = await fetch(
          `http://10.125.121.176:8080/api/v1/fish/fishListByDate?fish_name=${selectedFish}&weather_date=${selectedFishDate}`
        );
        const result = await response.json();

        // 지도용 데이터 업데이트 (이 데이터에 '매우좋음', '좋음' 등급이 포함됨)
        setFishLocationData(result.data || []);
        console.log("지도 업데이트 완료:", result.data);
      } catch (err) {
        console.error("지도 데이터 로드 실패:", err);
      }
    };

    getMapData();
  }, [selectedFish, selectedFishDate]); // 어종이나 날짜가 바뀌면 지도 갱신


  return (
    // 배경색을 이미지 2번과 유사한 연한 그레이(#F8F9FB)로 설정
    <section className="w-full min-h-screen bg-[#F8F9FB] py-16 px-6 md:px-12">
      <div className="max-w-[1600px] mx-auto">

        {/* 차트 카드 (이미지 2번의 Listed vs Offer Price 스타일) */}
        {/* <div className="lg:col-span-8 bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-50 flex flex-col h-[550px]">
          <div className="flex justify-between items-center mb-8">
            <div>
              <h3 className="text-xl font-bold text-slate-800">바다낚시 지수분석 챠트</h3>
              <div className="flex gap-4 mt-2">
                <div className="flex items-center gap-2 text-xs text-gray-500">
                  <span className="w-3 h-3 bg-[#FF5A3D] rounded-full"></span> 일주일 예측챠트
                </div> */}
        <div className="lg:col-span-8 p-8 rounded-[2.5rem] shadow-sm border border-blue-500 border-opacity-30 flex flex-col h-[550px]"
          style={{ background: 'linear-gradient(135deg, #1e3a5f 0%, #2d5a7b 100%)' }}>
          <div className="flex justify-between items-center mb-8">
            <div>
              <h3 className="text-xl font-bold text-white">바다낚시 지수분석 차트</h3>
              <div className="flex gap-4 mt-2">
                <div className="flex items-center gap-2 text-xs text-blue-100">
                  <span className="w-3 h-3 bg-[#FF5A3D] rounded-full"></span> 일주일 예측차트
                </div>
              </div>
            </div>
          </div>
          <div className="flex justify-center w-full mb-8">
            <select className="w-72 h-10 bg-white border-none text-sm rounded-xl px-4 py-2.5 pr-8 shadow-sm cursor-pointer focus:ring-2 focus:ring-blue-100 font-medium text-gray-700 min-w-[140px]"
              value={selectedArea} onChange={(e) => {
                setSelectedArea(e.target.value)
              }}>
              {
                areas &&
                areas.map((area) => (
                  <option key={area.area_no} value={area.area_no}>{area.area_name}</option>
                ))}
            </select>
            {/* 지역권 연동 추가 */}
            {

              locations &&
              <select className="w-72 h-10 bg-white border-none text-sm rounded-xl px-4 py-2.5 pr-8 shadow-sm cursor-pointer focus:ring-2 focus:ring-blue-100 font-medium text-gray-700 min-w-[140px]"
                value={selectedLocation} onChange={(e) => {
                  setSelectedLocation(e.target.value)
                }}>
                {locations.map((location) => (
                  <option key={location.location_no} value={location.location_no}>{location.name}</option>
                ))}
              </select>
            }
          </div>
          <div className="mb-10">
            <ChartPage selectedLocation={selectedLocation} />
          </div>
        </div>
      </div>

      {/* 지도 카드 (이미지 2번의 Properties & Location 스타일) */}
      <div className="lg:col-span-4 bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-50 flex flex-col h-[900px]">
        <div className="flex justify-between items-center mb-8">
          <h3 className="text-xl font-bold text-slate-800">KaKaoMap</h3>
          <button className="text-gray-400">•••</button>
        </div>
        {/* 어종 선택 콤보박스 */}
        <div className="flex justify-center gap-2 mb-4">
          <select
            className="w-40 h-10 bg-white border-none text-sm rounded-xl px-4 py-2.5 pr-8 shadow-sm cursor-pointer focus:ring-2 focus:ring-blue-100 font-medium text-gray-700"
            value={selectedFish}
            onChange={(e) => setSelectedFish(e.target.value)}
          >
            <option value="">어종 선택</option>
            {fishList && fishList.map((fish) => (
              <option key={fish.data_no} value={fish.name}>
                {fish.name}
              </option>
            ))}
          </select>
          <select
            value={selectedFishDate}
            onChange={(e) => setSelectedFishDate(e.target.value)}
          >
            <option value="">날짜 선택</option>
            {fishDateGroup.map((d) => (
              <option key={d} value={d}>
                {d}
              </option>
            ))}
          </select>
        </div>
          <div className="flex-[2] min-h-[700px] bg-slate-50 rounded-3xl relative flex items-center justify-center overflow-hidden">
          {/* 어종별 위치 데이터를 KakaoMapArea에 전달 */}
          <KakaoMapArea
            selectedLocation={selectedLocation}
            fishLocations={fishLocationData}
            selectedFish={selectedFish}
          />
          {/* 줌 버튼 */}
          <div className="absolute bottom-6 right-6 flex flex-col gap-2">
            <button className="w-10 h-10 bg-white rounded-full shadow-md flex items-center justify-center text-orange-500 font-bold">+</button>
            <button className="w-10 h-10 bg-white rounded-full shadow-md flex items-center justify-center text-gray-400 font-bold">-</button>
          </div>
        </div>
      </div>
    </section >
  );
}
