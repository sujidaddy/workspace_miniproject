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

export const locationData = atom(null);

export const fetchLocationData = atom( async() => {
    let url = 'http://localhost:8080/api/v1/location';
    const response = await fetch(url);
    const data: LocationData[] = await response.json();
    return data;
});

export const areaData = atom(null);

export const fetchAreaData = atom( async() => {
    let url = 'http://localhost:8080/api/v1/area';;
    const response = await fetch(url);
    const data: AreaData[] = await response.json();
    return data;
});