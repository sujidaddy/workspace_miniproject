/**
 * 바다낚시 지수 데이터 모델 정의
 */

// 리전 정의
export interface Region {
  area_no: number;
  area_name: string;
  center_lat: number;
  center_lng: number;
  radius: number;
  fillColor: string;
  level: number;
}
export interface Location {
  location_no: number;
  name: string;
  lat: number;
  lot: number;
  area_no: number;
  area_name: string;
}
// 1. 낚시 지수 등급 타입
export type FishingGrade = '매우좋음' | '좋음' | '보통' | '나쁨' | '매우나쁨';

// 2. 대상 어종별 지수 인터페이스
export interface SpeciesIndex {
  speciesName: string;    // 어종명 (예: 넙치, 농어, 감성돔 등)
  grade: FishingGrade;    // 해당 어종의 낚시 지수
}

// 3. 지역별 종합 낚시 정보 인터페이스
// 3. 지역별 종합 낚시 정보 인터페이스
export interface SeaFishingInfo {
  seafsPstnNm: string;      // 낚시터 위치명
  lat: number;              // 위도
  lot: number;              // 경도
  predcYmd:number;         // 예측 날짜
  predcNoonSeCd: string;    // 예측 시간대 (오전/오후)
  seafsTgfshNm: string;     // 대상 어종명
  tdlvHrScr: number;        // 시간대별 점수
  minWvhgt:number ;         // 최소 파고
  maxWvhgt: number;         // 최대 파고
  minWtem: number;          // 최소 수온
  maxWtem: number;          // 최대 수온
  minArtmp: number;         // 최소 기온
  maxArtmp: number;         // 최대 기온
  minCrsp: number;          // 최소 조류세기
  maxCrsp: number;          // 최대 조류세기
  minWspd: number;          // 최소 풍속
  maxWspd: number;          // 최대 풍속
  totalIndex: number;       // 종합 지수
  lastScr:number;          // 최종 점수
}

/**
 * 지수에 따른 UI 색상 매핑 유틸리티
 */
export const getGradeColor = (grade: FishingGrade): string => {
  const colorMap: Record<FishingGrade, string> = {
    '매우좋음': '#007AFF', // Blue
    '좋음': '#34C759',     // Green
    '보통': '#FFCC00',     // Yellow
    '나쁨': '#FF9500',     // Orange
    '매우나쁨': '#FF3B30'  // Red
  };
  return colorMap[grade];
};