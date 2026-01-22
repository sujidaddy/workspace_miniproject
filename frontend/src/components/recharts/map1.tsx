
import React, { useState, useEffect, useMemo } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { fetchWeatherChartData } from '@/ApiList';
//0115 데이터 연동
interface WaveData {
  predcYmd: string;
  minValue: number;
  maxValue: number;
}
interface Map1Props {
  selectedLocation: string;  
  title?: string; 
  color?: string;
}

// const Map1: React.FC<MapProps> = (selectedLocation: number) => {
function Map1({ selectedLocation, title, color  }: Map1Props) {
//  function Map1 = (selectedLocation: number) => {
  const [waveData, setWaveData] = useState<WaveData[]>([]);
  const [loading, setLoading] = useState(false);

  // 파고 데이터 fetch
  useEffect(() => {
    const fetchWaveData = async () => {
      if (!selectedLocation) return;
      
      setLoading(true);
      try {
        const result = await fetchWeatherChartData(selectedLocation, '파고');
        if (result && result.data) {
          setWaveData(result.data);
        }
      } catch (error) {
        console.error('파고 데이터 로딩 실패:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchWaveData();
  }, [selectedLocation]);



  // 표시할 데이터 결정 (props로 받은 data가 있으면 우선 사용, 없으면 API에서 가져온 데이터 사용)
 
  console.log("waveData", waveData);


  return (
    <div style={{ width: '400px', height: '200px' }}>
    <ResponsiveContainer width="100%" height="100%">
        <AreaChart data={waveData} margin={{ top: 10, right: 30, left: 0, bottom: 20 }}>
        <CartesianGrid strokeDasharray="3 3" />
        
        <XAxis  dataKey="predcYmd" 
                tick={{ fontSize: 8, fill: '#ffffff' }}/>
        <YAxis 
          label={{ value: '파고 (m)', angle: -90, position: 'insideLeft',
            style: { fontSize: 8 } }} tick={{ fontSize: 8, fill: '#ffffff' }} 
        />
        
        <Tooltip />
        <Legend 
          verticalAlign="top"
          wrapperStyle={{
            fontSize: '10px',
            fontFamily: 'Pretendard, sans-serif',
            fontWeight: '600',
            paddingTop: '0px'
          }}
        />
        
        {/* 최대값 영역 */}
        <Area 
          type="monotone" 
          dataKey="maxValue" 
          name="최대파고(m)"
          stroke="#8884d8" 
          fill="#8884d8" 
          fillOpacity={0.3}
          isAnimationActive={true}
        />
        
        {/* 최소값 영역 */}
        <Area 
          type="monotone" 
          dataKey="minValue" 
          name="최소파고(m)"
          stroke="#82ca9d" 
          fill="#82ca9d" 
          fillOpacity={0.3}
          isAnimationActive={true}
        />
      </AreaChart>
    </ResponsiveContainer>
    </div>
  );
};

export default Map1;
