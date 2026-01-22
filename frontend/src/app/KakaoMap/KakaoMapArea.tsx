// 
"use client"

import React, { useState, useEffect } from "react"; // [해결] React 명시적 가져오기 추가
import { Map, useKakaoLoader, Circle, CustomOverlayMap } from "react-kakao-maps-sdk"
<<<<<<< HEAD
=======
import { useState, useRef } from "react";
>>>>>>> 93772b8e32b270eaca1a1591be514e21dc05134e
import { useAtom } from "jotai";
import { fetchAreaData } from "./atomLocation";
import Popup from "@/components/Popup";

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

<<<<<<< HEAD
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
=======
    const [isMapLoaded, setIsMapLoaded] = useState(false);

    // 리셋 함수
    const handleReset = () => {
        setMapData(INITIAL_MAP_DATA);
        setSelecAreaNo(0);
        setIsCircleHover(false);
        setIsMapLoaded(false);
    };

    // 권역 선택시 처리
    const onClickCircle = (area_no: number) => {
        setIsCircleHover(false);
        //console.log("Circle clicked! Area No:", area_no);
        setSelecAreaNo(area_no);
        const selectArea = area?.find(a => a.area_no === area_no);
        const moveLocation = {
            center: { lat: selectArea?.center_lat!, lng: selectArea?.center_lng! },
            level: selectArea?.level ?? INITIAL_MAP_DATA.level,
            draggable: true,
            zoomable: true,
            disableDoubleClick : false,
        };
        //setMapData(moveLocation as { center: { lat: number; lng: number }; level: number; dragable: boolean; zoomable: boolean;});
        setMapData(moveLocation);
>>>>>>> 93772b8e32b270eaca1a1591be514e21dc05134e
    }
};

    useEffect(() => {
        getAuthHeaders().then(data => setTokenData(data));
    }, []);

    if (error) return <div className="p-4 text-red-500 text-xs font-bold">지도 로딩 실패</div>;
    if (loading) return <div className="p-4 text-xs font-medium">지도를 불러오는 중...</div>;

    return (
<<<<<<< HEAD
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
=======
        // 권역 선택 후 초기화시 마우스 커서 동작을 위한 스타일 처리 추가
        <div className={`w-full h-full flex relative ${isCircleHover ? 'map-cursor-pointer' : ''}`}>
            <style>{`
                .map-cursor-pointer, .map-cursor-pointer * {
                    cursor: pointer !important;
                }
            `}</style>
            {/* 카카오 맵 */}
            <Map // 지도를 표시할 Container
                onCreate={() => setIsMapLoaded(true)}
                center={mapData.center}
                level={mapData.level} // 지도의 확대 레벨
                draggable={mapData.draggable}
                zoomable={mapData.zoomable}
                disableDoubleClick={mapData.disableDoubleClick}
                // 지도 크기
                style={{
                    // 지도의 크기
                    width: "1800px",
                    height: "950px",
                }}
                // 위치 이동, 크기 변경시의 데이터 저장
                // 초기화를 위해서 필요
                onCenterChanged={(map) => {
                    const newCenter = map.getCenter();
                    setMapData(prev => ({
                        ...prev,
                        center: {
                            lat: newCenter.getLat(),
                            lng: newCenter.getLng(),
                        },
                    }));
                }}
                onZoomChanged={(map) => {
                    setMapData(prev => ({
                        ...prev,
                        level: map.getLevel(),
                    }));
                }}
            >
                {
                    // 권역별 영역 원 표시
                    isMapLoaded && 
                    selecAreaNo === 0 && area &&
                    area.map((item) => {
                        //console.log(item);
                        return(
                        <div key={item.area_no}>
                            <Circle
                                center={{ lat: item.center_lat, lng: item.center_lng}} // 원의 중심좌표
                                radius={item.radius}
                                strokeWeight={2}
                                strokeColor='#FF6347'
                                strokeOpacity={1}
                                strokeStyle={"solid"}
                                fillColor={item.fillColor}
                                fillOpacity={0.7}
                                // 권역 선택
                                onClick={() => onClickCircle(item.area_no)}
                                // 마우스 커서 처리
                                onMouseover={() => {setIsCircleHover(true)}}
                                onMouseout={() => {setIsCircleHover(false)}}
                            />
                            {/* 권역 이름 표시 */}
                            <CustomOverlayMap
                                position={{ lat: item.center_lat, lng: item.center_lng}} // 마커가 표시될 위치입니다
                            >
                                <div style={{ color: 'black', fontWeight: 'bold', textShadow: '1px 1px 2px #333', pointerEvents: 'none' }}>
                                    {item.area_name}
                                </div>
                            </CustomOverlayMap>
                        </div>
                    )})
                }
                {
                    // 표시할 위치 데이터
                    // 권역 선택시 해당 권역의 위치만 표시
                    location && 
                    location.filter(data => data.area.area_no == selecAreaNo).map(data => {
                        return (
                            <CustomOverlayMap // 마커를 생성합니다
                                key={data.location_no}
                                position={{
                                    // 마커가 표시될 위치입니다
                                    lat: data.lat,
                                    lng: data.lot,
                                }}
                            >
                                <div className='rounded-2xl border 
                                            bg-amber-50/50'>
                                    <span className="text-center text-black m-2">{data.name}</span>
                                </div>
                            </CustomOverlayMap>
                        )
                    })
                }
            </Map>
            {/* 초기화 버튼 */}
            <button
                onClick={() =>setIsPopup(true)}
                className="absolute top-4 right-4 bg-white text-black px-4 py-2 rounded-md shadow-md z-10 hover:bg-gray-100"
            >
                초기화
            </button>
            {/* 초기화 팝업 */}
            {isPopup &&
                <Popup
                    message="초기화 하시겠습니까?"
                    onConfirm={() => {
                        handleReset();
                        setIsPopup(false);
                    }}
                    onCancel={() => setIsPopup(false)}
                />
            }
>>>>>>> 93772b8e32b270eaca1a1591be514e21dc05134e
        </div>
    )
}