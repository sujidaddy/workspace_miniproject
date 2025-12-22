"use client"

import { Map, useKakaoLoader, Circle, CustomOverlayMap } from "react-kakao-maps-sdk"
import { useState, useEffect } from "react";
import { useAtom } from "jotai";
import { fetchLocationData, LocationData } from "./atomLocation";
import { fetchAreaData, AreaData } from "./atomLocation";
import Popup from "@/components/Popup";
import getAuthHeaders from "@/components/Token";
import type { TokenType } from "@/components/Token";

export default function KakaoMapArea() {
    // 카카오 지도 로드
    const [loading, error] = useKakaoLoader({
        appkey: process.env.NEXT_PUBLIC_KAKAO_APP_KEY!, // 발급 받은 APPKEY
        libraries: ["services", "clusterer"],
    })

    // 초기 위치 설정
    const INITIAL_MAP_DATA = {
        center: { lat: 35.8683, lng: 127.6358 },
        level: 13,
        draggable: false,
        zoomable: false,
        disableDoubleClick:true,
    };
    // 초기 위치 데이터 설정
    const [mapData, setMapData] = useState(INITIAL_MAP_DATA);
    
    // 지도상에 표시할 위치 데이터 로딩
    const [location] = useAtom(fetchLocationData);
    // 권역별 정보 데이터 로딩
    const [area] = useAtom(fetchAreaData);
    // 선택한 권역 번호 
    const [selecAreaNo, setSelecAreaNo] = useState<number>(0);
    // 팝업 상태
    const [isPopup, setIsPopup] = useState<boolean>(false);
    // 권역 원에 들어갔을때 마우스 커서 변경을 위한 상태값
    const [isCircleHover, setIsCircleHover] = useState<boolean>(false); 

    const [tokenData, setTokenData] = useState<TokenType | null>(null);

    useEffect(() => {
        getAuthHeaders()
        .then(data => {
            console.log("header in KakaoMapArea = ", data);
            setTokenData(prev => data);
        });
    }, []);
    

    // 리셋 함수
    const handleReset = () => {
        setMapData(INITIAL_MAP_DATA);
        setSelecAreaNo(0);
        setIsCircleHover(false);
    };

    // 권역 선택시 처리
    const onClickCircle = (area_no: number) => {
        setIsCircleHover(false);
        //console.log("Circle clicked! Area No:", area_no);
        setSelecAreaNo(area_no);
        const selectArea = area.find(a => a.area_no === area_no);
        const moveLocation = {
            center: { lat: selectArea?.center_lat!, lng: selectArea?.center_lng! },
            level: selectArea?.level ?? INITIAL_MAP_DATA.level,
            draggable: true,
            zoomable: true,
            disableDoubleClick : false,
        };
        //setMapData(moveLocation as { center: { lat: number; lng: number }; level: number; dragable: boolean; zoomable: boolean;});
        setMapData(moveLocation);
    }

    if (error) return <div>지도를 불러오는 중 에러가 발생했습니다. (도메인 등록 확인 필요)</div>;
    if (loading) return <div>로딩 중...</div>;
    return (
        <div>
            <span className="text-2xl font-bold">{tokenData?.username}님 안녕하세요</span>
            {/* 권역 선택 후 초기화시 마우스 커서 동작을 위한 스타일 처리 추가 */}
            <div className={`w-full h-full flex relative ${isCircleHover ? 'map-cursor-pointer' : ''}`}>
                <style>{`
                    .map-cursor-pointer, .map-cursor-pointer * {
                        cursor: pointer !important;
                    }
                `}</style>
                {/* 카카오 맵 */}
                <Map // 지도를 표시할 Container
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
                        selecAreaNo === 0 &&
                        area.map((item) => {
                            //console.log(item);
                            return(
                            <div key={item.area_name}>
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
                        location.filter(data => data.area_no == selecAreaNo).map(data => {
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
            </div>
        </div>
    )
}