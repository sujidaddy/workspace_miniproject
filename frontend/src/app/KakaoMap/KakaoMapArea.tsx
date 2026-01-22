// 
"use client"

import React, { useState, useEffect } from "react"; // [해결] React 명시적 가져오기 추가
import { Map, useKakaoLoader, Circle, CustomOverlayMap } from "react-kakao-maps-sdk"
import { useAtom } from "jotai";
import { fetchAreaData } from "./atomLocation";
import Popup from "@/components/Popup";
import getAuthHeaders from "@/components/Token";
import type { TokenType } from "@/components/Token";

interface KaKaoMapAreaProps {
    fishLocations: Location[]; 
    selectedFish: string;
    selectesdLocation: string;
  
}
interface Location {
  // 위치 관련 속성들
  name: string;
  lat: number;
  lng: number;
}

export default function KakaoMapArea({ fishLocations, selectedFish }: KaKaoMapAreaProps) {
    const [loading, error] = useKakaoLoader({
        appkey: process.env.NEXT_PUBLIC_KAKAO_APP_KEY!,
        libraries: ["services", "clusterer"],
    })

    const INITIAL_MAP_DATA = {
        center: { lat: 35.8683, lng: 127.6358 },
        level: 13,
    };

    const [mapData, setMapData] = useState(INITIAL_MAP_DATA);
    const [area] = useAtom(fetchAreaData);
    const [selecAreaNo, setSelecAreaNo] = useState<number>(0);
    const [isPopup, setIsPopup] = useState<boolean>(false);
    const [tokenData, setTokenData] = useState<TokenType | null>(null);

    const getStatusStyle = (totalIndex: string) => {
    switch (totalIndex) {
        case "매우좋음": return { stroke: "#06B6D4", fill: "#22D3EE", bg: "bg-cyan-400" };
        case "좋음": return { stroke: "#0284C7", fill: "#0EA5E9", bg: "bg-sky-500" };
        case "보통": return { stroke: "#2563EB", fill: "#3B82F6", bg: "bg-blue-500" };
        case "나쁨": return { stroke: "#7C3AED", fill: "#8B5CF6", bg: "bg-violet-500" };
        case "매우나쁨": return { stroke: "#BE185D", fill: "#DB2777", bg: "bg-pink-600" };
        default: return { stroke: "#4B5563", fill: "#9CA3AF", bg: "bg-gray-500" };
    }
};

    useEffect(() => {
        getAuthHeaders().then(data => setTokenData(data));
    }, []);

    if (error) return <div className="p-4 text-red-500 text-xs font-bold">지도 로딩 실패</div>;
    if (loading) return <div className="p-4 text-xs font-medium">지도를 불러오는 중...</div>;

    return (
        <div className="w-full h-full flex flex-col">
            {/* 상단 텍스트 폰트 크기 축소 */}
            <div className="mb-2 px-4 flex justify-between items-center">
                <span className="text-[11px] font-bold text-slate-700">
                    어종별 바다낚시 지수 _ 어종과 날짜를 선택하세요
                </span>
                {selectedFish && (
                    <span className="text-[11px] font-bold text-blue-600">현재 어종: {selectedFish}</span>
                )}
            </div>

            <div className="flex-1 relative rounded-3xl overflow-hidden shadow-inner border border-slate-200">
                <Map
                    center={mapData.center}
                    level={mapData.level}
                    style={{ width: "100%", height: "100%" , minHeight:'600px'}}
                >
                    {/* [해결] NaN 오류 방지 및 렌더링 구조 최적화 */}
                    {fishLocations && Array.isArray(fishLocations) && fishLocations.length > 0 ? (
                        fishLocations.map((loc: any, idx: number) => {
                            const style = getStatusStyle(loc.totalIndex);
                            const lat = Number(loc.lat);
                            const lng = Number(loc.lot); // API 필드명 'lot' 확인

                            // 좌표가 NaN이면 렌더링하지 않음
                            if (isNaN(lat) || isNaN(lng)) return null;

                            const position = { lat, lng };

                            return (
                                <React.Fragment key={`fish-point-${idx}`}>
                                    <Circle
                                        center={position}
                                        radius={4000}
                                        strokeWeight={2}
                                        strokeColor={style.stroke}
                                        fillColor={style.fill}
                                        fillOpacity={0.4}
                                    />
                                    <CustomOverlayMap position={position} yAnchor={2.6}>
                                        {/* 폰트 크기 text-[9px]로 축소 */}
                                        <div className={`px-2 py-1 rounded-lg shadow-md border border-white text-white font-bold text-[9px] ${style.bg} flex flex-col items-center min-w-[50px]`}>
                                            <span className="opacity-80 text-[8px]">{loc.seafsPstnNm}</span>
                                            <span>{loc.totalIndex}</span>
                                        </div>
                                    </CustomOverlayMap>
                                </React.Fragment>
                            );
                        })
                    ) : (
                        /* 데이터 없을 때 기본 권역 표시 */
                        selecAreaNo === 0 && area.map((item: any) => (
                            <React.Fragment key={item.area_name}>
                                <Circle 
                                    center={{ lat: item.center_lat, lng: item.center_lng }} 
                                    radius={item.radius} 
                                    strokeColor="#FF6347" 
                                    fillColor={item.fillColor} 
                                    fillOpacity={0.5} 
                                />
                            </React.Fragment>
                        ))
                    )}
                </Map>

                <button
                    onClick={() => setIsPopup(true)}
                    className="absolute top-4 right-4 bg-white/90 text-[11px] font-bold px-3 py-1.5 rounded-xl shadow-md z-10 border border-slate-200 hover:bg-white"
                >
                    지도 초기화
                </button>

                {isPopup && (
                    <Popup
                        message="지도 위치를 처음으로 되돌릴까요?"
                        onConfirm={() => { setMapData(INITIAL_MAP_DATA); setSelecAreaNo(0); setIsPopup(false); }}
                        onCancel={() => setIsPopup(false)}
                    />
                )}
            </div>
        </div>
    )
}