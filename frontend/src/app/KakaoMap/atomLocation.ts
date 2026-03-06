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
    area: AreaData,
}

export const locationData = atom(null);

export const fetchLocationData = atom( async() => {
    try {
        const response = await fetch('http://localhost:8080/api/v1/location', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if(!response.ok) {
            return null;
        }

        const result = await response.json();
        console.log(result);
        if(!result.success)
            alert('위치 정보 로딩에 실패했습니다.');
        else
        {
            const data: LocationData[] = result.data;
            return data;
        }

    } catch(error) {
        console.log(error);
    }
    return null;
});

export const areaData = atom(null);

export const fetchAreaData = atom( async() => {
    try {
        const response = await fetch('http://localhost:8080/api/v1/area', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if(!response.ok) {
            return null;
        }

        const result = await response.json();
        if(!result.success)
            alert('권역 정보 로딩에 실패했습니다.');
        else
        {
            const data: AreaData[] = result.data;
            return data;
        }

    } catch(error) {
        console.log(error);
    }
    return null;
});