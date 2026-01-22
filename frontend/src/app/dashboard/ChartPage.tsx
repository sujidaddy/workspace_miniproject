'use client'
import { Area, AreaChart, Brush, CartesianGrid, Line, LineChart, Tooltip, XAxis, YAxis } from 'recharts';
import Map1 from '../../components/recharts/map1';
import Map2 from '../../components/recharts/map2';
import Map3 from '../../components/recharts/map3';
import Map4 from '@/components/recharts/map4';
// #region Sample data: 물때지수, 바다낚시지수, 

interface ChartPageProps {
  selectedLocation: string;  
}
// #endregion
//export default function ChartPage (selectedLocation){
export default function ChartPage({ selectedLocation }: ChartPageProps) {


  return (
  <div className="grid grid-cols-2 gap-4 p-4">
    {/* 차트 4개 그리드 배치 (2행 2열) */}
    <Map1 
      title="파고(Wave Height)" 
      color="#8884d8"  // 파란색 계열
      selectedLocation={selectedLocation}
    />
    <Map2 
      title="풍속(Wind Speed)" 
      color="#82ca9d"  // 초록색 계열
      selectedLocation={selectedLocation}
    />
    <Map3 
      title="수온(Water Temp)" 
      color="#ffc658"  // 노란색 계열
      selectedLocation={selectedLocation}
    />
    <Map4 
      title="유속(Current Speed)" 
      color="#ff7300"  // 오렌지색 계열
      selectedLocation={selectedLocation}
    />
  </div>
);
}