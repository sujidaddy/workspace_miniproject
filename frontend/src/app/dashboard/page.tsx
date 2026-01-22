"use client";

import { useState, useMemo, useEffect } from "react";
import Sidebar from "@/components/SideBar"; 
import ForecastPanel from "@/components/ForecastPanel"; 
// import  from "@/components/KakaoMapFiltered";
// import FishingChart from "@/components/FishingChart";


// API로부터 데이터를 가져오는 함수 (이전에 atomLocation.ts에서 사용한 패턴 활용)
async function fetchData<T>(url: string, errorMsg: string): Promise<T[]> {
    try {
        const response = await fetch(url, {credentials: "include"});
        if (!response.ok) {
            // 500
            throw new Error(`HTTP 오류! 상태: ${response.status}`);
        }
        const data = await response.json();
        console.log("받은 데이터:", data); // 디버깅용
        
        // 배열인지 확인
        if (Array.isArray(data)) {
            return data;
        }
       // data.data, data.result 등의 형태일 경우
        if (data.data && Array.isArray(data.data)) {
            return data.data;
        }
        
        if (data.result && Array.isArray(data.result)) {
            return data.result;
        }
        
        // 배열이 아니면 빈 배열 반환
        console.error("데이터가 배열 형태가 아닙니다:", data);
        return [];
        
    } catch (error) {
        console.error(errorMsg, error);
        return [];
    }
}

export default function DashboardPage() {
  const [filterSpecies, setFilterSpecies] = useState("전체");
  // API로부터 받아온 원본 데이터를 저장할 상태
  const [fishingData, setFishingData] = useState<any[]>([]);

  // 컴포넌트 마운트 시 API로부터 데이터를 가져옴
  useEffect(() => {
    // API 엔드포인트는 실제 백엔드 주소에 맞게 수정해야 합니다.
    const apiUrl = "http://10.125.121.176:8080/api/v1/seafishing"; 
    fetchData(apiUrl, "낚시 데이터 가져오기 실패:").then(data => {
        setFishingData(data);
    });
  }, []); // 빈 배열을 전달하여 한 번만 실행되도록 함

  // [핵심] 필터링 로직: filterSpecies가 바뀔 때마다 filteredData가 자동 계산됨
  // 이 부분은 변경할 필요가 없습니다. fishingData 상태가 업데이트되면 자동으로 다시 계산됩니다.
  const filteredData = useMemo(() => {
    if (filterSpecies === "전체") return fishingData;
    // item.seafsTgfshNm는 실제 데이터의 어종 필드명에 맞게 확인/수정해야 합니다.
    return fishingData.filter((item) => item.seafsTgfshNm === filterSpecies);
  }, [filterSpecies, fishingData]); // fishingData가 변경될 때도 재계산되도록 종속성 추가

  return (
    <div className="flex h-screen bg-[#F8F9FB] overflow-hidden">
      {/* Sidebar에 상태와 변경 함수를 모두 전달해야 연동됨 */}
      <Sidebar 
        selectedSpecies={filterSpecies} 
        onSpeciesChange={setFilterSpecies} 
      />

      <main className="flex-1 overflow-y-auto p-12">
        {/* 상단 파도 영역에 현재 선택된 어종 반영 */}
        <div className="w-full h-[350px] bg-slate-800 rounded-[3rem] mb-10 shadow-2xl relative overflow-hidden">
           <div className="absolute inset-0 bg-black/20 flex flex-col justify-center px-12 text-white z-10">
             <h2 className="text-5xl font-black">{filterSpecies} 통합 예측</h2>
             <p className="mt-4 text-slate-200">선택된 어종에 최적화된 포인트를 분석 중입니다.</p>
           </div>
           {/* 배경 파도 영상 위치 */}
        </div>

        {/* 2. 파도 아래 3분할 패널 (필터링된 데이터를 Props로 전달) */}
        <ForecastPanel data={filteredData} />

        {/* 3. 하단 지도 및 상세 정보 */}
        <div className="grid grid-cols-12 gap-8">
          <div className="lg:col-span-8 bg-white p-8 rounded-[3rem] h-[550px] shadow-sm">
             <h3 className="text-xl font-bold mb-4">전국 {filterSpecies} 분포도</h3>
             {/* 지도 컴포넌트에 filteredData를 전달하여 마커 갱신 */}
          </div>
          <div className="lg:col-span-4 bg-white p-8 rounded-[3rem] h-[550px] shadow-sm">
             {/* 차트 컴포넌트에 filteredData를 전달하여 통계 갱신 */}
          </div>
        </div>
      </main>
    </div>
  );
}
