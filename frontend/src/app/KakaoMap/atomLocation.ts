// 'use client'
// import {atom} from "jotai";



// export interface AreaData {
//     area_no: number,
//     area_name: string,
//     center_lat: number,
//     center_lng: number,
//     radius: number,
//     fillColor: string,
//     level: number,
// };

// export interface LocationData {
//     location_no: number,
//     name: string,
//     lat: number,
//     lot: number,
//     area_no: number,
// }
// //0119 원본
// // async function fetchaData<T>(url: string, errorMsg: string): Promise<T[]> {
// //     try {
// //         const response = await fetch(url, {credentials: "include"});
// //         console.log("response", response);
// //         if (!response.ok) {
// //             throw new Error(`HTTP 오류! 상태: ${response.status}`);
// //         }
// //         const data: T[] = await response.json();
// //         console.log("data", data);
// //         return data;
// //     } catch (error) {
// //         console.error(errorMsg, error);
// //         return [];
// //     }
// // }

// // export const locationData = atom(null);

// // export const fetchLocationData = atom(async () => {
// //     const url = 'http://10.125.121.176:8080/api/v1/location';
// //     return fetchData<LocationData>(url, "위치 데이터 가져오기 실패:");
// // });

// // export const areaData = atom(null);

// // export const fetchAreaData = atom(async () => {
// //     const url = 'http://10.125.121.176:8080/api/v1/area';
// //     return fetchData<AreaData>(url, "권역 데이터 가져오기 실패:");
// // });
// // Area 데이터 fetch atom
// export const fetchAreaData = atom<AreaData[]>(async () => {
//   try {
//     const response = await fetch("http://10.125.121.176:8080/api/v1/area");
//     const result = await response.json();
//     return result.data || []; // 빈 배열 반환 보장
//   } catch (error) {
//     console.error("Area 데이터 로드 실패:", error);
//     return []; // 에러 시 빈 배열 반환
//   }
// });
// export const fetchFishLocationSearch = async (location_no: number) => {
//     try {
//         const response = await fetch(
//             `http://10.125.121.176:8080/api/v1/fish/location?location_no=${location_no}`,
//             {
//                 method: 'GET',
//                 headers: {
//                     'Content-Type': 'application/json',
//                 },
//             }
//         );

//         if (!response.ok)
//             return null;

//         return await response.json();
//     } catch (error) {
//         console.log(error);
//         return null;
//     }
// }
// // Location 데이터 fetch atom
// export const fetchLocationData = atom<LocationData[]>(async () => {
//   try {
//     const response = await fetch("http://10.125.121.176:8080/api/v1/location");
//     const result = await response.json();
//     return result.data || [];
//   } catch (error) {
//     console.error("Location 데이터 로드 실패:", error);
//     return [];
//   }
// });
'use client'
import {atom} from "jotai";

export interface AreaData {
    area_no: number,
    area_name: string,
    center_lat: number,
    center_lng: number,
    radius: number,
    fillColor: string,
    level: number,
};

export interface LocationData {
    location_no: number,
    name: string,
    lat: number,
    lot: number,
    area_no: number,
}

// Area 데이터 fetch atom
export const fetchAreaData = atom<AreaData[]>(async () => {
  try {
    const response = await fetch("http://10.125.121.176:8080/api/v1/area");
    const result = await response.json();
    return result.data || [];
  } catch (error) {
    console.error("Area 데이터 로드 실패:", error);
    return [];
  }
});

// Location 데이터 fetch atom
export const fetchLocationData = atom<LocationData[]>(async () => {
  try {
    const response = await fetch("http://10.125.121.176:8080/api/v1/location");
    const result = await response.json();
    return result.data || [];
  } catch (error) {
    console.error("Location 데이터 로드 실패:", error);
    return [];
  }
});

// Fish Location 조회 함수 (일반 async 함수)
export const fetchFishLocationSearch = async (location_no: number) => {
  try {
    const response = await fetch(
      `http://10.125.121.176:8080/api/v1/fish/location?location_no=${location_no}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );

    if (!response.ok) {
      console.error("Fish Location 조회 실패:", response.status);
      return null;
    }

    const result = await response.json();
    return result.data || null;
  } catch (error) {
    console.error("Fish Location 데이터 로드 실패:", error);
    return null;
  }
};
